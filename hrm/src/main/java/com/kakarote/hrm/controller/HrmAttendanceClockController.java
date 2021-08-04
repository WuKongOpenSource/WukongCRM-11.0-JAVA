package com.kakarote.hrm.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.hrm.constant.ClockType;
import com.kakarote.hrm.entity.BO.QueryAttendancePageBO;
import com.kakarote.hrm.entity.PO.HrmAttendanceClock;
import com.kakarote.hrm.entity.VO.QueryAttendancePageVO;
import com.kakarote.hrm.service.IHrmAttendanceClockService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 打卡记录表 前端控制器
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/hrmAttendanceClock")
@Slf4j
public class HrmAttendanceClockController {

    @Autowired
    private IHrmAttendanceClockService attendanceClockService;

    @PostMapping("/add")
    @ApiOperation("添加打卡")
    public Result add(@RequestBody HrmAttendanceClock attendanceClock){
        attendanceClockService.addOrUpdate(attendanceClock);
        return Result.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改打卡")
    public Result update(@RequestBody HrmAttendanceClock attendanceClock){
        attendanceClockService.addOrUpdate(attendanceClock);
        return Result.ok();
    }

    @PostMapping("/delete/{clockId}")
    @ApiOperation("删除打卡")
    public Result update(@PathVariable String clockId){
        attendanceClockService.removeById(clockId);
        return Result.ok();
    }

    @PostMapping("/queryPageList")
    @ApiOperation("查询打卡列表")
    public Result<BasePage<QueryAttendancePageVO>> queryPageList(@RequestBody QueryAttendancePageBO attendancePageBO){
        BasePage<QueryAttendancePageVO> page = attendanceClockService.queryPageList(attendancePageBO);
        return Result.ok(page);
    }

    @PostMapping("/queryMyPageList")
    @ApiOperation("查询自己打卡列表(手机端使用)")
    public Result<BasePage<QueryAttendancePageVO>> queryMyPageList(@RequestBody PageEntity pageEntity){
        BasePage<QueryAttendancePageVO> page = attendanceClockService.queryMyPageList(pageEntity);
        return Result.ok(page);
    }

    @PostMapping("/excelExport")
    @ApiOperation("导出")
    public void excelExport(@RequestBody QueryAttendancePageBO attendancePageBO,HttpServletResponse response){
        attendancePageBO.setPageType(0);
        List<QueryAttendancePageVO> list = attendanceClockService.queryPageList(attendancePageBO).getList();
        List<Map<String, Object>> collect = list.stream().map(clock -> {
            Map<String, Object> map = BeanUtil.beanToMap(clock);
            String clockType = ClockType.valueOfName((Integer) map.remove("clockType"));
            map.put("clockType", clockType);
            map.remove("clockId");
            map.remove("clockEmployeeId");
            map.remove("type");
            map.remove("lng");
            map.remove("lat");
            map.remove("remark");
            return map;
        }).collect(Collectors.toList());
        try (ExcelWriter writer = ExcelUtil.getWriter()) {
            writer.addHeaderAlias("employeeName", "姓名");
            writer.addHeaderAlias("jobNumber", "工号");
            writer.addHeaderAlias("deptName", "部门");
            writer.addHeaderAlias("post", "岗位");
            writer.addHeaderAlias("attendanceTime", "上班时间");
            writer.addHeaderAlias("clockType", "打卡类型");
            writer.addHeaderAlias("clockTime", "打卡时间");
            writer.addHeaderAlias("address", "打卡地点");
            writer.merge(9, "员工信息");
            writer.setOnlyAlias(true);
            writer.write(collect, true);
            writer.setRowHeight(0, 30);
            writer.setRowHeight(1, 20);
            for (int i = 0; i < 10; i++) {
                writer.setColumnWidth(i, 20);
            }
            Cell cell = writer.getCell(0, 0);
            CellStyle cellStyle = cell.getCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = writer.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            //自定义标题别名
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=attendance_clock.xls");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out);
        } catch (Exception e) {
            log.error("导出客户错误：", e);
        }
    }
}

