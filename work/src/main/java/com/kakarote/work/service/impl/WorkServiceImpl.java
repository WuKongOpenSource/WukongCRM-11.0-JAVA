package com.kakarote.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.feign.crm.service.CrmService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.core.utils.*;
import com.kakarote.work.common.WorkAuthUtil;
import com.kakarote.work.common.WorkCodeEnum;
import com.kakarote.work.common.WorkUtil;
import com.kakarote.work.entity.BO.*;
import com.kakarote.work.entity.PO.*;
import com.kakarote.work.entity.VO.*;
import com.kakarote.work.mapper.WorkMapper;
import com.kakarote.work.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author wyq
 * @since 2020-05-15
 */
@Service
public class WorkServiceImpl extends BaseServiceImpl<WorkMapper, Work> implements IWorkService {
    @Autowired
    private IWorkTaskClassService workTaskClassService;

    @Autowired
    private IWorkUserService workUserService;

    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private IWorkCollectService workCollectService;

    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private IWorkTaskLabelService workTaskLabelService;

    @Autowired
    private IWorkService workService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private WorkAuthUtil workAuthUtil;

    @Autowired
    private AdminFileService adminFileService;

    @Autowired
    private CrmService crmService;

    @Override
    public Work addWork(Work work) {
        Long userId = UserUtil.getUserId();
        if (StringUtils.isEmpty(work.getName()) || null == work.getName()) {
//            throw new CrmException(WorkCodeEnum.WORK_CREATE_NAME_NULL_ERROR);
        }
        if (StrUtil.isEmpty(work.getBatchId())) {
            work.setBatchId(IdUtil.simpleUUID());
        }
        Set<Long> ownerUserIds = new HashSet<>();
        ownerUserIds.add(userId);
        if (work.getOwnerUserId() != null) {
            ownerUserIds.addAll(SeparatorUtil.toLongSet(work.getOwnerUserId()));
        }
        if (work.getIsOpen() == 1) {
            //公开项目删除负责人
            ownerUserIds.clear();
        }
        work.setOwnerUserId(SeparatorUtil.fromLongSet(ownerUserIds));
        work.setCreateUserId(userId);
        work.setCreateTime(new Date());
        save(work);
        WorkTaskClass workTaskClass = new WorkTaskClass();
        workTaskClass.setClassId(0);
        workTaskClass.setName("要做");
        workTaskClass.setCreateTime(new Date());
        workTaskClass.setCreateUserId(userId);
        workTaskClass.setWorkId(work.getWorkId());
        workTaskClass.setOrderNum(1);
        workTaskClassService.save(workTaskClass);
        workTaskClass.setName("在做");
        workTaskClass.setOrderNum(2);
        workTaskClassService.save(workTaskClass);
        workTaskClass.setName("待定");
        workTaskClass.setOrderNum(3);
        workTaskClassService.save(workTaskClass);
        ownerUserIds.forEach(ownerUserId -> {
            WorkUser workUser = new WorkUser();
            workUser.setWorkId(work.getWorkId());
            workUser.setUserId(ownerUserId);
            if (ownerUserId.equals(userId)) {
                workUser.setRoleId(workAuthUtil.getSmallAdminRole());
                workUserService.save(workUser);
            } else {
                workUser.setRoleId(workAuthUtil.getSmallEditRole());
                workUserService.save(workUser);
            }
        });
        return work.setWorkOwnerRoleList(queryOwnerRoleList(work.getWorkId()));
    }

    @Override
    public WorkInfoVo queryById(Integer workId) {
        Work simpleWork = getById(workId);
        if (simpleWork == null) {
            throw new CrmException(WorkCodeEnum.WORK_EXIST_ERROR);
        }
        WorkInfoVo work = BeanUtil.copyProperties(simpleWork, WorkInfoVo.class);
        if (work.getIsSystemCover() != null && work.getIsSystemCover() == 0) {
            FileEntity fileEntity = adminFileService.queryOne(work.getBatchId()).getData();
            if (fileEntity != null) {
                work.setFileId(fileEntity.getFileId());
                work.setCoverUrl(fileEntity.getUrl());
            }
        }
        work.setOwnerUser(adminService.queryUserByIds(TagUtil.toLongSet(work.getOwnerUserId())).getData());
        int isUpdate = 0;
        UserInfo user = UserUtil.getUser();
        if (workAuthUtil.isWorkAdmin() || user.getRoles().contains(workAuthUtil.getSmallAdminRole())) {
            isUpdate = 1;
        }
        Long userId = user.getUserId();
        Integer roleId = workUserService.getRoleId(userId, workId);
        List<Integer> adminMenus = Collections.singletonList(301);
        String[] menuArr = new String[]{"setWork", "excelExport", "saveTaskClass", "updateTaskClass", "updateClassOrder", "deleteTaskClass", "saveTask", "setTaskStatus", "setTaskTitle", "setTaskDescription", "setTaskMainUser", "setTaskTime", "setTaskLabel", "setTaskOwnerUser", "setTaskPriority", "setTaskOrder", "archiveTask", "deleteTask", "cleanTask", "uploadTaskFile", "deleteTaskFile", "excelImport", "addChildTask", "updateChildTask", "deleteChildTask", "restoreTask", "saveTaskRelation", "setChildTaskStatus","manageTaskOwnerUser"};
        JSONObject object = new JSONObject();
        List<String> menuRecords = getBaseMapper().queryMenuByRoleId(roleId);
        adminMenus.forEach(menu -> {
            JSONObject authObject = new JSONObject();
            List<String> childMenus = Arrays.asList(menuArr);
            if ((roleId != null && roleId.equals(workAuthUtil.getSmallAdminRole())) || UserUtil.isAdmin() || user.getRoles().contains(workAuthUtil.getWorkAdminRole())) {
                childMenus.forEach(child -> {
                    authObject.put(child, true);
                });
            } else {
                if (work.getIsOpen() == 1) {
                    if (work.getOwnerRole().equals(workAuthUtil.getSmallAdminRole())) {
                        childMenus.forEach(child -> {
                            authObject.put(child, true);
                        });
                    } else {
                        List<String> realmList = getBaseMapper().queryWorkAuthByUserId(work.getOwnerRole(), menu);
                        realmList.forEach(realm -> {
                            authObject.put(realm, true);
                        });
                    }
                } else {
                    menuRecords.forEach(r -> {
                        if (Arrays.asList(menuArr).contains(r)) {
                            authObject.put(r, true);
                        }
                    });
                }
            }
            if (!authObject.isEmpty()) {
                object.put("project", authObject);
            }
        });
        work.setPermission(new JSONObject().fluentPut("isUpdate", isUpdate));
        work.setAuthList(object);
        return work;
    }

    @Override
    public Work updateWork(Work work) {
        Long userId = UserUtil.getUserId();
        Integer workId = work.getWorkId();
        Work oldWork = getById(workId);
        if (work.getOwnerUserId() != null) {
            if (!"".equals(work.getOwnerUserId())) {
                work.setOwnerUserId(SeparatorUtil.fromString(work.getOwnerUserId()));
                Set<Long> oldOwnerUserIds = SeparatorUtil.toLongSet(oldWork.getOwnerUserId());
                Set<Long> ownerUserIds = SeparatorUtil.toLongSet(work.getOwnerUserId());
                Collection<Long> intersection = CollectionUtil.intersection(oldOwnerUserIds, ownerUserIds);
                oldOwnerUserIds.removeAll(intersection);
                ownerUserIds.removeAll(intersection);
                for (Long next : oldOwnerUserIds) {
                    leave(work.getWorkId(), next);
                }
                for (Long ownerUserId : ownerUserIds) {
                    WorkUser workUser = new WorkUser();
                    workUser.setWorkId(work.getWorkId());
                    workUser.setUserId(ownerUserId);
                    workUser.setRoleId(workAuthUtil.getSmallEditRole());
                    workUserService.save(workUser);
                }
            } else {
                workUserService.remove(new QueryWrapper<WorkUser>().eq("work_id", workId));
                work.setOwnerUserId("," + userId + ",");
                WorkUser workUser = new WorkUser();
                workUser.setWorkId(work.getWorkId());
                workUser.setUserId(userId);
                workUser.setRoleId(workAuthUtil.getSmallAdminRole());
                workUserService.save(workUser);
            }
        }

        //判断私有转公开还是公开转私有
        if (work.getIsOpen() != null && !work.getIsOpen().equals(oldWork.getIsOpen())) {
            if (work.getIsOpen() == 1) {
                //公开项目删除负责人
                work.setOwnerUserId(null);
                workUserService.remove(new QueryWrapper<WorkUser>().eq("work_id", workId));
            } else if (work.getIsOpen() == 0) {
                List<Long> userList = adminService.queryUserList(1).getData();
                userList.remove(userId);
                List<WorkUser> workUserList = new ArrayList<>();
                WorkUser nowWorkUser = new WorkUser();
                nowWorkUser.setWorkId(work.getWorkId());
                nowWorkUser.setUserId(userId);
                nowWorkUser.setRoleId(workAuthUtil.getSmallAdminRole());
                workUserList.add(nowWorkUser);
                userList.forEach(id -> {
                    WorkUser workUser = new WorkUser();
                    workUser.setWorkId(work.getWorkId());
                    workUser.setUserId(id);
                    workUser.setRoleId(workAuthUtil.getSmallEditRole());
                    workUserList.add(workUser);
                });
                workUserService.saveBatch(workUserList, Const.BATCH_SAVE_SIZE);
                userList.add(userId);
                work.setOwnerUserId(SeparatorUtil.fromLongSet(userList));
            }
        }
        if (Objects.equals(3, work.getStatus())) {
            work.setArchiveTime(new Date());
        }
        //处理之前上传的封面
        if (Objects.equals(0, work.getIsSystemCover())){
            List<FileEntity> fileEntities = adminFileService.queryFileList(work.getBatchId()).getData();
            if (fileEntities != null && fileEntities.size() > 1){
                fileEntities.sort(Comparator.comparing(FileEntity::getCreateTime).reversed());
                fileEntities.remove(0);
                fileEntities.forEach(fileEntity -> adminFileService.delete(fileEntity.getFileId()));
            }
        }
        updateById(work);
        return work.setWorkOwnerRoleList(queryOwnerRoleList(workId));
    }

    @Override
    public void deleteWork(Integer workId) {
        getBaseMapper().deleteTaskRelationByWorkId(workId);
        workTaskService.remove(new QueryWrapper<WorkTask>().eq("pid", workId));
        workTaskService.remove(new QueryWrapper<WorkTask>().eq("work_id", workId));
        workTaskClassService.remove(new QueryWrapper<WorkTaskClass>().eq("work_id", workId));
        workUserService.remove(new QueryWrapper<WorkUser>().eq("work_id", workId));
        removeById(workId);
    }

    @Override
    public void leave(Integer workId, Long userId) {
        Work work = getById(workId);
        if (work.getCreateUserId().equals(userId)) {
            throw new CrmException(WorkCodeEnum.WORK_CREATE_USER_EXIT_ERROR);
        }
        workTaskService.update(null, Wrappers.<WorkTask>lambdaUpdate().set(WorkTask::getOwnerUserId, null).eq(WorkTask::getWorkId, workId).eq(WorkTask::getMainUserId, userId));
        getBaseMapper().leaveTaskOwnerUser(workId, userId);
        Set<Long> ownerUserIds = SeparatorUtil.toLongSet(work.getOwnerUserId());
        ownerUserIds.remove(userId);
        work.setOwnerUserId(SeparatorUtil.fromLongSet(ownerUserIds));
        updateById(work);
        workUserService.remove(new QueryWrapper<WorkUser>().eq("work_id", workId).eq("user_id", userId));
    }

    @Override
    public List<WorkInfoVo> queryWorkNameList(WorkTaskQueryBO workTaskQueryBO) {
        List<WorkInfoVo> workInfoList;
        Long userId = UserUtil.getUserId();
        QueryWrapper<WorkCollect> wapper = new QueryWrapper<WorkCollect>().eq("user_id", userId);
        wapper.select("work_id");
        List<Integer> collectWorkIdList = workCollectService.listObjs(wapper, obj -> Integer.valueOf(obj.toString()));
        List<WorkOrder> workOrder;
        if (workAuthUtil.isWorkAdmin()) {
            workOrder = workOrderService.list(new QueryWrapper<WorkOrder>().eq("user_id", userId));
            workInfoList = getBaseMapper().queryWorkNameList(null, workTaskQueryBO);
        } else {
            workOrder = workOrderService.list(new QueryWrapper<WorkOrder>().eq("user_id", userId));
            workInfoList = getBaseMapper().queryWorkNameList(UserUtil.getUserId(), workTaskQueryBO);
        }
        if (CollUtil.isNotEmpty(workTaskQueryBO.getUserIdList())) {
            if (CollUtil.isNotEmpty(workInfoList)) {
                workInfoList = workInfoList.stream().filter(t -> WorkUtil.verifyIntersection(t.getOwnerUserId(), workTaskQueryBO.getUserIdList())).collect(Collectors.toList());
            }
        }
        List<Integer> adminMenus = new ArrayList<>();
        adminMenus.add(301);
        workInfoList.forEach(workInfo -> {
            if (Objects.equals(0, workInfo.getIsSystemCover())) {
                FileEntity fileEntity = adminFileService.queryOne(workInfo.getBatchId()).getData();
                workInfo.setCoverUrl(fileEntity.getUrl());
            }
            workOrder.forEach(order -> {
                if (order.getWorkId().equals(workInfo.getWorkId())) {
                    workInfo.setOrderNum(order.getOrderNum());
                }
            });
            if (collectWorkIdList.contains(workInfo.getWorkId())) {
                workInfo.setCollect(1);
            }
            //拼接当前用户在每个项目的权限
            Integer workId = workInfo.getWorkId();
            UserInfo user = UserUtil.getUser();
            Integer roleId = workUserService.getRoleId(userId, workId);
            String[] menuArr = new String[]{"setWork", "excelExport", "saveTaskClass", "updateTaskClass", "updateClassOrder", "deleteTaskClass", "saveTask", "setTaskStatus", "setTaskTitle", "setTaskDescription", "setTaskMainUser", "setTaskTime", "setTaskLabel", "setTaskOwnerUser", "setTaskPriority", "setTaskOrder", "archiveTask", "deleteTask", "cleanTask", "uploadTaskFile", "deleteTaskFile", "excelImport", "addChildTask", "updateChildTask", "deleteChildTask", "restoreTask", "saveTaskRelation", "setChildTaskStatus"};
            JSONObject object = new JSONObject();
            List<String> menuRecords = getBaseMapper().queryMenuByRoleId(roleId);
            adminMenus.forEach(menu -> {
                JSONObject authObject = new JSONObject();
                List<String> childMenus = Arrays.asList(menuArr);
                if ((roleId != null && roleId.equals(workAuthUtil.getSmallAdminRole())) || UserUtil.isAdmin() || user.getRoles().contains(workAuthUtil.getWorkAdminRole())) {
                    childMenus.forEach(child -> {
                        authObject.put(child, true);
                    });
                } else {
                    if (workInfo.getIsOpen() == 1) {
                        if (workInfo.getOwnerRole().equals(workAuthUtil.getSmallAdminRole())) {
                            childMenus.forEach(child -> {
                                authObject.put(child, true);
                            });
                        } else {
                            List<String> realmList = getBaseMapper().queryWorkAuthByUserId(workInfo.getOwnerRole(), menu);
                            realmList.forEach(realm -> {
                                authObject.put(realm, true);
                            });
                        }
                    } else {
                        menuRecords.forEach(r -> {
                            if (Arrays.asList(menuArr).contains(r)) {
                                authObject.put(r, true);
                            }
                        });
                    }
                }
                if (!authObject.isEmpty()) {
                    object.put("project", authObject);
                }
            });
            workInfo.setAuthList(object);
        });
        return workInfoList.stream().sorted(Comparator.comparingInt(WorkInfoVo::getOrderNum)).collect(Collectors.toList());
    }


    @Override
    public List<TaskInfoVO> queryWorkTaskList(WorkTaskQueryBO workTaskQueryBO) {
        //项目
        List<Integer> workIdList;
        if (CollUtil.isNotEmpty(workTaskQueryBO.getWorkIdList())) {
            workIdList = workTaskQueryBO.getWorkIdList();
        } else {
            List<WorkInfoVo> workInfoList;
            if (workAuthUtil.isWorkAdmin()) {
                workInfoList = getBaseMapper().queryWorkNameList(null, new WorkTaskQueryBO());
            } else {
                workInfoList = getBaseMapper().queryWorkNameList(UserUtil.getUserId(), new WorkTaskQueryBO());
            }
            if (CollUtil.isEmpty(workInfoList)) {
                return new ArrayList<>();
            }
            workIdList = workInfoList.stream().map(WorkInfoVo::getWorkId).collect(Collectors.toList());
        }
        Integer taskSort = workTaskQueryBO.getSort();
        if (taskSort != null) {
            workTaskQueryBO.setSort(taskSort + 1);
        }
        List<TaskInfoVO> taskList = getBaseMapper().queryWorkTaskByCondition(workTaskQueryBO, workIdList);
        if (CollUtil.isNotEmpty(taskList)) {
            taskList.forEach(taskInfo -> {
                if (taskInfo.getWorkId() != null) {
                    Work work = workService.getById(taskInfo.getWorkId());
                    if (work != null) {
                        taskInfo.setWorkName(work.getName());
                    }
                }
                if (taskInfo.getBatchId() != null) {
                    List<FileEntity> fileEntities = adminFileService.queryFileList(taskInfo.getBatchId()).getData();
                    taskInfo.setFileNum(fileEntities.size());
                }
            });
        }
        return taskList;
    }

    @Override
    public List<WorkTaskTemplateClassVO> queryTaskByWorkId(WorkTaskTemplateBO workTaskTemplateBO) {
        Integer workId = workTaskTemplateBO.getWorkId();
        List<WorkTaskTemplateClassVO> classVOList = getBaseMapper().queryWorkTaskTemplateClass(workId);
        LinkedList<WorkTaskTemplateClassVO> linkedList = new LinkedList<>(classVOList);
        WorkTaskTemplateClassVO item = new WorkTaskTemplateClassVO();
        item.setClassId(-1).setClassName("(未分组)");
        linkedList.addFirst(item);
        List<WorkTaskTemplateClassVO> finalClassList = new CopyOnWriteArrayList<>(linkedList);
        finalClassList.forEach(workClass -> {
            List<TaskInfoVO> taskList = getBaseMapper().queryTaskByClassId(workTaskTemplateBO, workClass.getClassId());
            workClass.setCount(taskList.size());
            if (taskList.size() == 0) {
                if (workClass.getClassId() != -1) {
                    workClass.setList(new ArrayList<>());
                } else {
                    finalClassList.remove(workClass);
                }
            } else {
                workTaskService.taskListTransfer(taskList);
                //先保证已完成的在最后，再按照order_num排序
                if (Objects.equals(workTaskTemplateBO.getSort(), 1)) {
                    taskList.sort(Comparator.comparingInt(TaskInfoVO::getOrderNum));
                }
                boolean completedTask = Optional.ofNullable(workTaskTemplateBO.getCompletedTask()).orElse(false);
                if (completedTask) {
                    List<TaskInfoVO> taskInfoVoS = taskList.stream().filter(t -> t.getStatus() == 5).collect(Collectors.toList());
                    taskList.removeIf(t -> t.getStatus() == 5);
                    taskList.addAll(taskInfoVoS);
                }
                workClass.setList(taskList);
            }
        });
        return finalClassList;
    }

    @Override
    public List<WorkTaskTemplateUserVO> queryOwnerTaskListByWorkId(WorkTaskTemplateBO workTaskTemplateBO) {
        List<TaskInfoVO> taskList = getBaseMapper().queryTaskByClassId(workTaskTemplateBO, null);
        if (taskList.size() == 0) {
            return new ArrayList<>();
        } else {
            workTaskService.taskListTransfer(taskList);
            taskList.removeIf(task -> ObjectUtil.isEmpty(task.getMainUserId()));
            Map<Long, List<TaskInfoVO>> map = taskList.stream().collect(Collectors.groupingBy(TaskInfoVO::getMainUserId));
            List<Long> userIdList = new ArrayList<>(map.keySet());
            List<WorkTaskTemplateUserVO> userList = new ArrayList<>();
            List<SimpleUser> data = adminService.queryUserByIds(userIdList).getData();
            data.forEach(simpleUser -> {
                WorkTaskTemplateUserVO userVO = new WorkTaskTemplateUserVO();
                userVO.setRealname(simpleUser.getRealname());
                userVO.setUserId(simpleUser.getUserId());
                userVO.setImg(simpleUser.getImg());
                userList.add(userVO);
            });
            userList.forEach(user -> {
                List<TaskInfoVO> userTaskList = map.get(user.getUserId());
                userTaskList.sort((r1, r2) -> {
                    if (r1.getStatus() == 5) {
                        return 1;
                    } else if (r2.getStatus() == 5) {
                        return -1;
                    } else {
                        if (r1.getUpdateTime() != null && r2.getUpdateTime() != null){
                            return -r1.getUpdateTime().compareTo(r2.getUpdateTime());
                        }else {
                            return 1;
                        }
                    }
                });
                user.setList(userTaskList).setCount(userTaskList.size());
            });
            return userList;
        }
    }

    @Override
    public BasePage<Work> queryArchiveWorkList(PageEntity pageEntity) {
        return page(pageEntity.parse(), new QueryWrapper<Work>().select("work_id", "archive_time", "name", "color").eq("status", "3"));
    }

    @Override
    public WorkStatsVO workStatistics(String workType) {
        WorkStatsVO workStatsVO = new WorkStatsVO();
        Long userId1 = UserUtil.getUserId();
        if ("all".equals(workType)) {
            WorkTaskStatsVO taskStatistics = new WorkTaskStatsVO();
            List<WorkUserStatsVO> memberTaskStatistics = new ArrayList<>();
            if (workAuthUtil.isWorkAdmin()) {
                taskStatistics = getBaseMapper().workStatistics(null, null, null, null);
                memberTaskStatistics = getBaseMapper().queryAllWorkMember();
                memberTaskStatistics.forEach(userStatsVO -> {
                    WorkTaskStatsVO first = getBaseMapper().workStatistics(null, null, null, userStatsVO.getUserId());
                    BeanUtil.copyProperties(first, userStatsVO);
                });
            } else {
                List<Integer> workIds = getBaseMapper().queryWorkIdListByOwnerUser(userId1);
                if (workIds.size() == 0) {
                    taskStatistics.setUnfinished(0).setOverdue(0).setComplete(0).setArchive(0).setCompletionRate("0").setOverdueRate("0");
                } else {
                    taskStatistics = getBaseMapper().workStatistics(null, workIds, null, null);
                    List<SimpleUser> simpleUserList = adminService.queryUserByIds(workTaskService.lambdaQuery().eq(WorkTask::getIshidden, 0).in(WorkTask::getWorkId, workIds).list().stream().map(WorkTask::getMainUserId).collect(Collectors.toList())).getData();
                    for (SimpleUser simpleUser : simpleUserList) {
                        memberTaskStatistics.add(BeanUtil.copyProperties(simpleUser, WorkUserStatsVO.class));
                    }
                    ;
                    memberTaskStatistics.forEach(userStatsVO -> {
                        WorkTaskStatsVO first = getBaseMapper().workStatistics(null, workIds, null, userStatsVO.getUserId());
                        BeanUtil.copyProperties(first, userStatsVO);
                    });
                }
            }
            return workStatsVO.setTaskStatistics(taskStatistics).setMemberTaskStatistics(memberTaskStatistics);
        }
        Integer workId = Integer.valueOf(workType);
        WorkTaskStatsVO taskStatistics = getBaseMapper().workStatistics(workId, null, null, null);
        String ownerUserId = getOne(new QueryWrapper<Work>().select("owner_user_id").eq("work_id", workId)).getOwnerUserId();
        Integer smallAdminRoleId = adminService.queryWorkRole(2).getData();
        List<Long> workAdminUserIds = workUserService.lambdaQuery().select(WorkUser::getUserId).eq(WorkUser::getRoleId, smallAdminRoleId).eq(WorkUser::getWorkId, workId)
                .list().stream().map(WorkUser::getUserId).collect(Collectors.toList());
        List<SimpleUser> ownerList = new ArrayList<>(adminService.queryUserByIds(workAdminUserIds).getData());
        List<SimpleUser> userList = new ArrayList<>(adminService.queryUserByIds(SeparatorUtil.toLongSet(ownerUserId)).getData());
        List<WorkClassStatsVO> classStatistics = new ArrayList<>();
        List<WorkLabelStatsVO> labelStatistics = new ArrayList<>();
        List<WorkTaskClass> recordList = workTaskClassService.list(new QueryWrapper<WorkTaskClass>().select("class_id", "name").eq("work_id", workId));
        Map<Integer, String> classMap = new HashMap<>();
        recordList.forEach(workTaskClass -> classMap.put(workTaskClass.getClassId(), workTaskClass.getName()));
        classMap.forEach((classId, name) -> {
            WorkClassStatsVO first = getBaseMapper().queryWorkClassStats(workId, classId);
            classStatistics.add(first.setClassName(name));
        });
        List<WorkTask> labelList = getBaseMapper().queryWorkLabelByWorkId(workId);
        List<String> labelIdList = labelList.stream().map(WorkTask::getLabelId).collect(Collectors.toList());
        Set<Integer> labelIdSet = new HashSet<>(WorkUtil.toList(labelIdList));
        Map<Integer, WorkLabelStatsVO> labelMap = new HashMap<>();
        labelIdSet.forEach(id -> {
            WorkTaskLabel workTaskLabel = workTaskLabelService.getOne(new QueryWrapper<WorkTaskLabel>().select("label_id", "name", "color").eq("label_id", id));
            WorkLabelStatsVO workLabelStatsVO = new WorkLabelStatsVO();
            BeanUtil.copyProperties(workTaskLabel, workLabelStatsVO);
            labelMap.put(workTaskLabel.getLabelId(), workLabelStatsVO);
        });
        labelMap.forEach((id, workLabelStatsVO) -> {
            AtomicReference<Integer> complete = new AtomicReference<>(0);
            AtomicReference<Integer> undone = new AtomicReference<>(0);
            labelList.forEach(label -> {
                if (label.getLabelId().contains(id.toString())) {
                    if (label.getStatus() == 1) {
                        undone.getAndSet(undone.get() + 1);
                    } else if (label.getStatus() == 5) {
                        complete.getAndSet(complete.get() + 1);
                    }
                }
            });
            labelStatistics.add(workLabelStatsVO.setComplete(complete.get()).setUndone(undone.get()));
        });
        List<WorkUserStatsVO> memberTaskStatistics = memberTaskStatistics(workId);
        workStatsVO.setTaskStatistics(taskStatistics).setClassStatistics(classStatistics).setLabelStatistics(labelStatistics).setMemberTaskStatistics(memberTaskStatistics)
                .setUserList(userList).setOwnerList(ownerList);
        return workStatsVO;
    }

    private List<WorkUserStatsVO> memberTaskStatistics(Integer workId) {
        List<WorkUserStatsVO> list = new ArrayList<>();
        Work work = getById(workId);
        if (work.getIsOpen() == 1) {
            List<SimpleUser> simpleUserList = adminService.queryUserByIds(workTaskService.lambdaQuery().eq(WorkTask::getIshidden, 0).eq(WorkTask::getWorkId, workId).list().stream().map(WorkTask::getMainUserId).collect(Collectors.toList())).getData();
            for (SimpleUser simpleUser : simpleUserList) {
                list.add(BeanUtil.copyProperties(simpleUser, WorkUserStatsVO.class));
            }
            ;
            list.forEach(userStatsVO -> {
                WorkTaskStatsVO first = getBaseMapper().workStatistics(workId, null, null, userStatsVO.getUserId());
                BeanUtil.copyProperties(first, userStatsVO);
            });
        } else {
            String ownerUserIds = getOne(new QueryWrapper<Work>().select("owner_user_id").eq("work_id", workId)).getOwnerUserId();
            if (StrUtil.isEmpty(ownerUserIds)) {
                return list;
            }
            for (String userId : ownerUserIds.split(",")) {
                if (StrUtil.isEmpty(userId)) {
                    continue;
                }
                SimpleUser user = adminService.queryUserById(Long.valueOf(userId)).getData();
                WorkTaskStatsVO first = getBaseMapper().workStatistics(workId, null, Long.parseLong(userId), null);
                WorkUserStatsVO workUserStatsVO = BeanUtil.copyProperties(first, WorkUserStatsVO.class);
                workUserStatsVO.setRealname(user.getRealname());
                workUserStatsVO.setImg(user.getImg());
                workUserStatsVO.setUserId(user.getUserId());
                list.add(workUserStatsVO);
            }
        }
        return list;
    }

    @Override
    public List<SimpleUser> queryWorkOwnerList(Integer workId) {
        String ownerUserId = getOne(new QueryWrapper<Work>().select("owner_user_id").eq("work_id", workId)).getOwnerUserId();
        return adminService.queryUserByIds(SeparatorUtil.toLongSet(ownerUserId)).getData();
    }

    @Override
    public List<SimpleUser> queryMemberListByWorkOrTask(boolean isWork) {
        StringBuilder ownerUserId = new StringBuilder();
        if (isWork) {
            List<WorkInfoVo> workInfoList;
            if (workAuthUtil.isWorkAdmin()) {
                workInfoList = getBaseMapper().queryWorkNameList(null, new WorkTaskQueryBO());
            } else {
                workInfoList = getBaseMapper().queryWorkNameList(UserUtil.getUserId(), new WorkTaskQueryBO());
            }
            for (WorkInfoVo workInfoVo : workInfoList) {
                if (StrUtil.isNotEmpty(workInfoVo.getOwnerUserId())) {
                    ownerUserId.append(workInfoVo.getOwnerUserId());
                }
            }
        } else {
            List<String> ownerUserList = getBaseMapper().queryTaskOwnerUser(UserUtil.getUserId());
            for (String ownerUser : ownerUserList) {
                if (StrUtil.isNotEmpty(ownerUser)) {
                    ownerUserId.append(ownerUser);
                }
            }
        }
        ownerUserId.append(UserUtil.getUserId()).append(Const.SEPARATOR);
        return adminService.queryUserByIds(SeparatorUtil.toLongSet(ownerUserId.toString())).getData();
    }

    @Override
    public void updateOrder(UpdateTaskClassBO updateTaskClassBO) {
        if (CollectionUtil.isNotEmpty(updateTaskClassBO.getFromList())) {
            List<Integer> fromList = updateTaskClassBO.getFromList();
            for (int i = 1; i <= fromList.size(); i++) {
                workTaskService.updateById(new WorkTask().setTaskId(fromList.get(i - 1)).setClassId(updateTaskClassBO.getFromId()).setOrderNum(i));
            }
        }
        if (CollectionUtil.isNotEmpty(updateTaskClassBO.getToList())) {
            List<Integer> toList = updateTaskClassBO.getToList();
            for (int i = 1; i <= toList.size(); i++) {
                workTaskService.updateById(new WorkTask().setTaskId(toList.get(i - 1)).setClassId(updateTaskClassBO.getToId()).setOrderNum(i));
            }
        }
    }

    @Override
    public List<WorkOwnerRoleBO> queryOwnerRoleList(Integer workId) {
        Work byId = getById(workId);
        if (workId == null || byId.getIsOpen() == 1) {
            List<Long> userList = adminService.queryUserList(1).getData();
            List<SimpleUser> data = adminService.queryUserByIds(userList).getData();
            List<WorkOwnerRoleBO> collect = data.stream().map(obj -> BeanUtil.copyProperties(obj, WorkOwnerRoleBO.class)).collect(Collectors.toList());
            if (byId != null && byId.getOwnerRole() != null) {
                Integer ownerRole = byId.getOwnerRole();
                String roleName = getBaseMapper().queryRoleName(ownerRole);
                collect.forEach(workOwnerRoleBO -> {
                    workOwnerRoleBO.setRoleId(ownerRole);
                    workOwnerRoleBO.setRoleName(roleName);
                });
            }
            return collect;
        }
        return getBaseMapper().queryOwnerRoleList(workId);
    }

    @Override
    public List<WorkOwnerRoleBO> setOwnerRole(SetWorkOwnerRoleBO setWorkOwnerRoleBO) {
        Integer workId = setWorkOwnerRoleBO.getWorkId();
        List<WorkUser> workUserList = setWorkOwnerRoleBO.getList();
        StringBuilder ownerUserId = new StringBuilder(",");
        for (WorkUser workUser : workUserList) {
            ownerUserId.append(workUser.getUserId()).append(",");
            workUser.setWorkId(workId);
        }
        lambdaUpdate().set(Work::getOwnerUserId, ownerUserId.toString()).eq(Work::getWorkId, workId).update();
        workUserService.remove(new QueryWrapper<WorkUser>().eq("work_id", workId));
        workUserService.saveBatch(workUserList, Const.BATCH_SAVE_SIZE);
        return queryOwnerRoleList(workId);
    }

    @Override
    public void deleteTaskList(DeleteTaskClassBO deleteTaskClassBO) {
        Integer workId = deleteTaskClassBO.getWorkId();
        Integer classId = deleteTaskClassBO.getClassId();
        WorkTask workTask = new WorkTask().setIshidden(1).setHiddenTime(new Date()).setClassId(-1);
        workTaskService.update(workTask, new QueryWrapper<WorkTask>().eq("class_id", classId).eq("work_id", workId).ne("is_archive", 1));
        workTaskClassService.removeById(classId);
    }

    @Override
    public void archiveTask(Integer classId) {
        WorkTask workTask = new WorkTask().setIsArchive(1).setArchiveTime(new Date());
        workTaskService.update(workTask, new QueryWrapper<WorkTask>().eq("class_id", classId).eq("status", 5).eq("ishidden", 0));
    }

    @Override
    public void archiveTask(ArchiveTaskByOwnerBO archiveTaskByOwnerBO) {
        Long userId = archiveTaskByOwnerBO.getUserId();
        Integer workId = archiveTaskByOwnerBO.getWorkId();
        WorkTask workTask = new WorkTask().setIsArchive(1).setArchiveTime(new Date());
        workTaskService.update(workTask, new QueryWrapper<WorkTask>().eq("main_user_id", userId).eq("work_id", workId).eq("status", 5).eq("ishidden", 0));
    }

    @Override
    public List<TaskInfoVO> archList(Integer workId) {
        List<TaskInfoVO> taskInfoVOList = getBaseMapper().archList(workId);
        workTaskService.taskListTransfer(taskInfoVOList);
        return taskInfoVOList;
    }

    @Override
    public void updateClassOrder(UpdateClassOrderBO updateClassOrderBO) {
        List<Integer> classIds = updateClassOrderBO.getClassIds();
        for (int i = 0; i < classIds.size(); i++) {
            workTaskClassService.update(new WorkTaskClass().setOrderNum(i), new QueryWrapper<WorkTaskClass>().eq("work_id", updateClassOrderBO.getWorkId()).eq("class_id", classIds.get(i)));
        }
    }

    @Override
    public void activation(Integer taskId) {
        Integer classId = workTaskService.getOne(new QueryWrapper<WorkTask>().select("class_id").eq("task_id", taskId)).getClassId();
        if (classId != null && classId > 0) {
            workTaskService.update(null, Wrappers.<WorkTask>lambdaUpdate().set(WorkTask::getIsArchive, 0).set(WorkTask::getArchiveTime, null).eq(WorkTask::getTaskId, taskId));
        } else {
            workTaskService.update(null, Wrappers.<WorkTask>lambdaUpdate().set(WorkTask::getIsArchive, 0).set(WorkTask::getArchiveTime, null).set(WorkTask::getClassId, null).eq(WorkTask::getTaskId, taskId));
        }
    }

    @Override
    public JSONObject auth(Integer workId) {
        Work work = getById(workId);
        UserInfo userInfo = UserUtil.getUser();
        JSONObject jsonObject = new JSONObject();
        List<Integer> roleIds = userInfo.getRoles();
        Integer workMenuId = 300;
        Integer projectMenuId = 301;
        JSONObject object = new JSONObject();
        String[] menuArr = new String[]{"setWork", "excelExport", "saveTaskClass", "updateTaskClass", "updateClassOrder", "deleteTaskClass", "saveTask", "setTaskStatus", "setTaskTitle", "setTaskDescription", "setTaskMainUser", "setTaskTime", "setTaskLabel", "setTaskOwnerUser", "setTaskPriority", "setTaskOrder", "archiveTask", "deleteTask", "cleanTask", "uploadTaskFile", "deleteTaskFile", "excelImport", "addChildTask", "updateChildTask", "deleteChildTask", "restoreTask", "saveTaskRelation", "setChildTaskStatus"};
        List<String> realmList = new ArrayList<>();
        if (UserUtil.isAdmin() || roleIds.contains(workAuthUtil.getWorkAdminRole())) {
            realmList = Arrays.asList(menuArr);
        } else if (work.getIsOpen() == 0) {
            Integer roleId = workUserService.getRoleId(userInfo.getUserId(), workId);
            if (Objects.equals(roleId, workAuthUtil.getSmallAdminRole())) {
                realmList = Arrays.asList(menuArr);
            } else {
                realmList = getBaseMapper().queryWorkAuthByUserId(roleId, projectMenuId);
            }
        } else if (work.getIsOpen() == 1) {
            if (work.getOwnerRole().equals(workAuthUtil.getSmallAdminRole())) {
                realmList = Arrays.asList(menuArr);
            } else {
                realmList = getBaseMapper().queryWorkAuthByUserId(work.getOwnerRole(), projectMenuId);
            }
        }
        realmList.forEach(realm -> {
            object.fluentPut(realm, true);
        });
        jsonObject.fluentPut("work", new JSONObject().fluentPut("project", object));
        return jsonObject;
    }

    @Override
    public Dict excelImport(MultipartFile file, Integer workId) throws IOException {
        List<List<Object>> errList = new ArrayList<>();
        Map<String, Integer> classMap = new HashMap<>();
        Work work = getById(workId);
        List<SimpleUser> workUserList = adminService.queryUserByIds(TagUtil.toLongSet(work.getOwnerUserId())).getData();
        Map<String, Long> workUserMap = workUserList.stream().collect(Collectors.toMap(SimpleUser::getRealname, SimpleUser::getUserId));
        if (file.isEmpty()) {
            return Dict.create();
        }
        AtomicReference<Integer> num = new AtomicReference<>(0);
        ExcelUtil.readBySax(file.getInputStream(), 0, (int sheetIndex, int rowIndex, List<Object> rowList) -> {
            if (rowIndex > 1001) {
                rowList.add(0, "最多同时导入1000条数据");
                errList.add(rowList);
                return;
            }
            if (rowIndex > 1) {
                if (rowList.size() < 7) {
                    for (int i = rowList.size() - 1; i < 7; i++) {
                        rowList.add(null);
                    }
                }
                num.getAndSet(num.get() + 1);
                if (StrUtil.isEmptyIfStr(rowList.get(0))) {
                    rowList.add(0, "任务名称不能为空");
                    errList.add(rowList);
                    return;
                }
                if (!StrUtil.isEmptyIfStr(rowList.get(2))) {
                    Object object = rowList.get(2);
                    String time;
                    if (object instanceof Long) {
                        time = cn.hutool.core.date.DateUtil.formatDate(org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) object));
                    } else {
                        time = object.toString();
                    }
                    if (!BaseUtil.isTime(time)) {
                        rowList.add(0, "开始时间日期格式错误，例:2000-01-01");
                        errList.add(rowList);
                        return;
                    }
                }
                if (!StrUtil.isEmptyIfStr(rowList.get(3))) {
                    Object object = rowList.get(3);
                    String time;
                    if (object instanceof Long) {
                        time = cn.hutool.core.date.DateUtil.formatDate(org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) object));
                    } else {
                        time = object.toString();
                    }
                    if (!BaseUtil.isTime(time)) {
                        rowList.add(0, "结束时间日期格式错误，例:2000-01-01");
                        errList.add(rowList);
                        return;
                    }
                }
                if (StrUtil.isEmptyIfStr(rowList.get(6))) {
                    rowList.add(0, "所属任务列表不能为空");
                    errList.add(rowList);
                    return;
                }
                String taskName = rowList.get(0).toString().trim();
                String description = rowList.get(1).toString().trim();
                Date startTime = null;
                if (ObjectUtil.isNotEmpty(rowList.get(2))) {
                    if (rowList.get(2) instanceof Long) {
                        startTime = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) rowList.get(2));
                    } else {
                        startTime = cn.hutool.core.date.DateUtil.parse(rowList.get(2).toString().trim());
                    }
                }
                Date stopTime = null;
                if (ObjectUtil.isNotEmpty(rowList.get(3))) {
                    if (rowList.get(3) instanceof Long) {
                        stopTime = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Long) rowList.get(3));
                    } else {
                        stopTime = cn.hutool.core.date.DateUtil.parse(rowList.get(3).toString().trim());
                    }
                }
                String mainUserName = rowList.get(4).toString().trim();
                Long mainUserId = UserUtil.getUserId();
                if (StrUtil.isNotEmpty(mainUserName)) {
                    if (ObjectUtil.isEmpty(workUserMap.get(mainUserName))) {
                        rowList.add(0, "负责人与当前项目成员不匹配");
                        errList.add(rowList);
                        return;
                    } else {
                        mainUserId = workUserMap.get(mainUserName);
                    }
                }
                String ownerUserName = rowList.get(5).toString().trim();
                if (StrUtil.isNotEmpty(ownerUserName)) {
                    List<String> ownerUserNameList = new ArrayList<>(Arrays.asList(ownerUserName.split(",")));
                    for (String userName : ownerUserNameList) {
                        if (ObjectUtil.isEmpty(workUserMap.get(userName))) {
                            rowList.add(0, "参与人与当前项目成员不匹配");
                            errList.add(rowList);
                            return;
                        }
                    }
                }
                String className = rowList.get(6).toString().trim();
                //任务列表 填写的列表名不存在则新增列表
                Integer classId = workTaskClassService.lambdaQuery().select(WorkTaskClass::getClassId)
                        .eq(WorkTaskClass::getName, className).eq(WorkTaskClass::getWorkId, workId).last("limit 1").oneOpt()
                        .map(WorkTaskClass::getClassId).orElse(null);
                List<String> classNameList = new ArrayList<>(classMap.keySet());
                if (!classNameList.contains(className) && classId == null) {
                    WorkTaskClass taskClass = new WorkTaskClass();
                    taskClass.setWorkId(workId);
                    taskClass.setName(className);
                    Integer orderNum = (Integer) workTaskClassService.listMaps(new QueryWrapper<WorkTaskClass>().select("max(order_num) as maxNum").eq("work_id", workId))
                            .get(0).get("maxNum");
                    taskClass.setOrderNum(orderNum + classNameList.size() + 1);
                    taskClass.setCreateUserId(UserUtil.getUserId());
                    taskClass.setCreateTime(new Date());
                    workTaskClassService.save(taskClass);
                    classId = taskClass.getClassId();
                    classMap.put(className, classId);
                } else if (classNameList.contains(className)) {
                    classId = classMap.get(className);
                }
                WorkTask task = new WorkTask();
                task.setWorkId(workId);
                task.setName(taskName);
                task.setDescription(description);
                task.setStartTime(startTime);
                task.setStopTime(stopTime);
                task.setMainUserId(mainUserId);
                List<Long> ownerUserIdList = adminService.queryUserIdByRealName(StrUtil.splitTrim(ownerUserName, ",")).getData();
                String ownerUserIds;
                if (CollUtil.isNotEmpty(ownerUserIdList)) {
                    ownerUserIds = TagUtil.fromLongSet(ownerUserIdList);
                } else {
                    ownerUserIds = ",";
                }
                task.setOwnerUserId(ownerUserIds);
                task.setClassId(classId);
                workTaskService.saveWorkTask(task);
            }
        });
        Dict result = Dict.create().set("totalSize", num.get()).set("errSize", 0);
        if (errList.size() > 0) {
            BigExcelWriter writer = null;
            try {
                String token = IdUtil.simpleUUID();
                writer = ExcelUtil.getBigWriter(BaseUtil.UPLOAD_PATH + "excel/" + token);

                writer.merge(7, "任务导入模板(*)为必填项");
                for (int i = 0; i < 8; i++) {
                    writer.setColumnWidth(i, 20);
                }
                writer.setDefaultRowHeight(20);
                Cell cell = writer.getCell(0, 0);
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = writer.createFont();
                font.setBold(true);
                font.setFontHeightInPoints((short) 16);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                writer.writeHeadRow(Arrays.asList("错误信息", "任务名称(*)", "任务描述", "开始时间", "结束时间", "负责人", "参与人", "所属任务列表(*)"));
                writer.write(errList);
                result.set("errSize", errList.size()).set("token", token);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        return result;
    }

    @Override
    public BasePage<FileEntity> queryTaskFileByWorkId(QueryTaskFileByWorkIdBO queryTaskFileByWorkIdBO) {
        List<String> batchIdList = workTaskService.lambdaQuery().select(WorkTask::getBatchId)
                .eq(WorkTask::getWorkId, queryTaskFileByWorkIdBO.getWorkId())
                .isNotNull(WorkTask::getBatchId)
                .list()
                .stream().map(WorkTask::getBatchId).collect(Collectors.toList());
        List<FileEntity> data = adminFileService.queryFileList(batchIdList).getData();
        int[] ints = PageUtil.transToStartEnd(queryTaskFileByWorkIdBO.getPage() - 1, queryTaskFileByWorkIdBO.getLimit());
        List<FileEntity> list = CollUtil.sub(data, ints[0], ints[1]);
        BasePage<FileEntity> page = queryTaskFileByWorkIdBO.parse();
        page.setTotal(data.size());
        page.setList(list);
        return page;
    }

    /**
     * 查询可导出任务
     *
     * @param workId 项目ID
     * @return data
     */
    @Override
    public List<Map<String, Object>> workTaskExport(Integer workId) {
        List<Map<String, Object>> recordList = getBaseMapper().workTaskExport(workId);
        List<WorkTaskLabel> taskLabels = workTaskLabelService.lambdaQuery().select(WorkTaskLabel::getLabelId, WorkTaskLabel::getName).list();
        /*
          缓存标签对象
         */
        Map<String, String> labelNames = new HashMap<>(taskLabels.size(), 1.0f);
        for (WorkTaskLabel taskLabel : taskLabels) {
            labelNames.put(taskLabel.getLabelId().toString(), taskLabel.getName());
        }
        recordList.forEach(record -> {
            //拼接关联业务内容
            StringBuilder relateCrmWork = new StringBuilder("");
            if (record.get("labelId") != null) {
                List<String> labelIds = StrUtil.splitTrim(record.get("labelId").toString(), Const.SEPARATOR);
                String labelName = labelIds.stream().map(labelNames::get).collect(Collectors.joining(Const.SEPARATOR));
                record.put("labelName", labelName);
            } else {
                record.put("labelName", "");
            }
            if (record.get("ownerUserId") != null) {
                List<String> ownerUserIds = StrUtil.splitTrim(record.get("ownerUserId").toString(), Const.SEPARATOR);
                String ownerUserName = ownerUserIds.stream().map(userId -> UserCacheUtil.getUserName(Long.valueOf(userId))).collect(Collectors.joining(Const.SEPARATOR));
                record.put("ownerUserName", ownerUserName);
            } else {
                record.put("ownerUserName", "");
            }
            if (record.containsKey("mainUserId")) {
                Long mainUserId = TypeUtils.castToLong(record.get("mainUserId"));
                record.put("mainUserName", UserCacheUtil.getUserName(mainUserId));
            } else {
                record.put("mainUserName", "");
            }
            if (record.containsKey("createUserId")) {
                Long createUserId = TypeUtils.castToLong(record.get("createUserId"));
                record.put("createUserName", UserCacheUtil.getUserName(createUserId));
            } else {
                record.put("createUserName", "");
            }

            if (record.get("customerIds") != null) {
                List<String> ids = StrUtil.splitTrim(record.get("customerIds").toString(), Const.SEPARATOR);
                if (ids.size() > 0) {
                    relateCrmWork.append("客户 ");
                    List<SimpleCrmEntity> simpleCrmEntityList = crmService.queryCustomerInfo(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
                    for (SimpleCrmEntity crmEntity : simpleCrmEntityList) {
                        relateCrmWork.append("【").append(crmEntity.getName()).append("】");
                    }
                    relateCrmWork.append("\n");
                }
            }
            if (record.get("contactsIds") != null) {
                List<String> ids = StrUtil.splitTrim(record.get("contactsIds").toString(), Const.SEPARATOR);
                if (ids.size() > 0) {
                    relateCrmWork.append("联系人 ");
                    List<SimpleCrmEntity> simpleCrmEntityList = crmService.queryContactsInfo(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
                    for (SimpleCrmEntity crmEntity : simpleCrmEntityList) {
                        relateCrmWork.append("【").append(crmEntity.getName()).append("】");
                    }
                    relateCrmWork.append("\n");
                }
            }
            if (record.get("businessIds") != null) {
                List<String> ids = StrUtil.splitTrim(record.get("businessIds").toString(), Const.SEPARATOR);
                if (ids.size() > 0) {
                    relateCrmWork.append("商机 ");
                    List<SimpleCrmEntity> simpleCrmEntityList = crmService.queryBusinessInfo(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
                    for (SimpleCrmEntity crmEntity : simpleCrmEntityList) {
                        relateCrmWork.append("【").append(crmEntity.getName()).append("】");
                    }
                    relateCrmWork.append("\n");
                }
            }
            if (record.get("contractIds") != null) {
                List<String> ids = StrUtil.splitTrim(record.get("contractIds").toString(), Const.SEPARATOR);
                if (ids.size() > 0) {
                    relateCrmWork.append("合同 ");
                    List<SimpleCrmEntity> simpleCrmEntityList = crmService.queryContractInfo(ids.stream().map(Integer::valueOf).collect(Collectors.toList())).getData();
                    for (SimpleCrmEntity crmEntity : simpleCrmEntityList) {
                        relateCrmWork.append("【").append(crmEntity.getName()).append("】");
                    }
                    relateCrmWork.append("\n");
                }
            }
            record.put("relateCrmWork", relateCrmWork.toString());
        });
        return recordList;
    }
}
