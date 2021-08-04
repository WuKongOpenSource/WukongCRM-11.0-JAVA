package com.kakarote.hrm.controller;

import com.kakarote.core.common.Result;
import com.kakarote.hrm.entity.BO.QueryNotesByTimeBO;
import com.kakarote.hrm.entity.BO.QueryNotesStatusBO;
import com.kakarote.hrm.entity.PO.HrmNotes;
import com.kakarote.hrm.entity.VO.*;
import com.kakarote.hrm.service.IHrmDashboardService;
import com.kakarote.hrm.service.IHrmNotesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/hrmDashboard")
@Api(tags = "人力资源仪表盘")
public class HrmDashboardController {

    @Autowired
    private IHrmDashboardService dashboardService;

    @Autowired
    private IHrmNotesService notesService;


    @PostMapping("/employeeSurvey")
    @ApiOperation("人事概况")
    public Result<Map<Integer, Long>> employeeSurvey() {
        Map<Integer, Long> map = dashboardService.employeeSurvey();
        return Result.ok(map);
    }

    @PostMapping("/recruitSurvey")
    @ApiOperation("招聘动态")
    public Result<RecruitSurveyCountVO> recruitSurvey() {
        RecruitSurveyCountVO recruitSurveyCountVO = dashboardService.recruitSurvey();
        return Result.ok(recruitSurveyCountVO);
    }

    @PostMapping("/lastSalarySurvey")
    @ApiOperation("上月薪资概况")
    public Result<LastSalarySurveyVO> lastSalarySurvey() {
        LastSalarySurveyVO lastSalarySurveyVO = dashboardService.lastSalarySurvey();
        return Result.ok(lastSalarySurveyVO);
    }


    @PostMapping("/appraisalCountSurvey")
    @ApiOperation("绩效概况统计")
    public Result<Map<Integer,Integer>> appraisalCountSurvey() {
        Map<Integer,Integer> map = dashboardService.appraisalCountSurvey();
        return Result.ok(map);
    }

    @PostMapping("/appraisalSurvey/{status}")
    @ApiOperation("绩效概况")
    public Result<List<AppraisalSurveyVO>> appraisalSurvey(@PathVariable String status) {
        List<AppraisalSurveyVO> appraisalSurveyVOList = dashboardService.appraisalSurvey(status);
        return Result.ok(appraisalSurveyVOList);
    }

    @PostMapping("/toDoRemind")
    @ApiOperation("待办提醒")
    public Result<DoRemindVO>  toDoRemind(){
        DoRemindVO doRemindVO = dashboardService.toDoRemind();
        return Result.ok(doRemindVO);
    }

    @PostMapping("/addNotes")
    @ApiOperation("添加备忘")
    public Result addNotes(@RequestBody HrmNotes notes){
        notesService.addNotes(notes);
        return Result.ok();
    }

    @PostMapping("/deleteNotes/{notesId}")
    @ApiOperation("添加备忘")
    public Result deleteNotes(@PathVariable String notesId){
        notesService.removeById(notesId);
        return Result.ok();
    }

    @PostMapping("/queryNotesByTime")
    @ApiOperation("查询当日事项")
    public Result<List<NotesVO>> queryNotesByTime(@RequestBody QueryNotesByTimeBO queryNotesByTimeBO){
        List<NotesVO> notesVOList = dashboardService.queryNotesByTime(queryNotesByTimeBO);
        return Result.ok(notesVOList);
    }

    @PostMapping("/queryNotesStatus")
    @ApiOperation("查询当日事项")
    public Result<Set<String>> queryNotesStatus(@RequestBody QueryNotesStatusBO queryNotesByTimeBO){
        Set<String> timeList = dashboardService.queryNotesStatus(queryNotesByTimeBO);
        return Result.ok(timeList);
    }


    @PostMapping("/myTeam")
    @ApiOperation("我的团队(上级角色)")
    public Result<Map<Integer, Long>> myTeam() {
        Map<Integer, Long> map = dashboardService.myTeam();
        return Result.ok(map);
    }

    @PostMapping("/teamSurvey")
    @ApiOperation("团队概况(上级角色)")
    public Result<TeamSurveyVO> teamSurvey() {
        TeamSurveyVO teamSurveyVO = dashboardService.teamSurvey();
        return Result.ok(teamSurveyVO);
    }

    @PostMapping("/mySurvey")
    @ApiOperation("我的概况(员工端)")
    public Result<MySurveyVO> mySurvey() {
        MySurveyVO data = dashboardService.mySurvey();
        return Result.ok(data);
    }

    @PostMapping("/myNotesByTime")
    @ApiOperation("查询当日事项(员工端)")
    public Result<List<NotesVO>> myNotesByTime(@RequestBody QueryNotesByTimeBO queryNotesByTimeBO){
        List<NotesVO> notesVOList = dashboardService.myNotesByTime(queryNotesByTimeBO);
        return Result.ok(notesVOList);
    }

    @PostMapping("/myNotesStatus")
    @ApiOperation("查询当日事项(员工端)")
    public Result<Set<String>> myNotesStatus(@RequestBody QueryNotesStatusBO queryNotesByTimeBO){
        Set<String> timeList = dashboardService.myNotesStatus(queryNotesByTimeBO);
        return Result.ok(timeList);
    }

}
