package com.xyongfeng.controller;


import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.aop.OperationLogAnnotation;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.AdminLogService;
import com.xyongfeng.service.MeetingChatService;
import com.xyongfeng.service.MeetingService;
import com.xyongfeng.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */

@Api(tags = "管理员操作meeting接口")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsMeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetingChatService meetingChatService;

    @OperationLogAnnotation(actionModule = "meeting", actionType = "查看", actionUrl = "/admins/meeting/#{#current}/#{#size}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("分页查看会议列表")
    @GetMapping("/meeting/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/meeting 查看会议列表。%d , %d", current, size));
        return meetingService.select(new PageParam(current, size));
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("添加会议")
    @PostMapping("/meeting")
    public JsonResult add(@RequestBody @Validated MeetingAddParam meeting) {
        log.info(String.format("post:/meeting 添加会议。%s", meeting));
        return meetingService.add(meeting);
    }

    @OperationLogAnnotation(actionModule = "meeting", actionType = "编辑", actionUrl = "/admins/meeting/#{#mid}",actionContent = "#{#meeting}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("修改会议")
    @PutMapping("/meeting/{mid}")
    public JsonResult update(@RequestBody @Validated MeetingUpdateParam meeting, @PathVariable String mid) {
        meeting.setId(mid);
        log.info(String.format("put:/meeting 修改会议。%s", meeting));
        return meetingService.update(meeting);
    }

    @OperationLogAnnotation(actionModule = "meeting", actionType = "删除", actionUrl = "/admins/meeting/#{#mid}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("删除会议")
    @DeleteMapping("/meeting/{mid}")
    public JsonResult delete(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 删除会议。%s", mid));
        return meetingService.delete(mid);
    }

    @OperationLogAnnotation(actionModule = "meeting", actionType = "编辑", actionUrl = "/admins/meeting/#{#mid}/licence/#{#haveLicence}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/{mid}/licence/{haveLicence}")
    public JsonResult setLicence(@PathVariable String mid, @PathVariable Integer haveLicence) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s %d", mid, haveLicence));
        return meetingService.setLicence(new MeetSetLicenceParam(mid, haveLicence == 1));
    }


    @OperationLogAnnotation(actionModule = "dataStatistics", actionType = "查看", actionUrl = "/admins/meeting/allStartDate")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看所有会议的开始时间")
    @GetMapping("/meeting/allStartDate")
    public JsonResult selectAllStartDateTime() {
        log.info("get:/meeting/allStartDate 查看所有会议的开始时间。");
        return meetingService.selectAllStartDateTime();
    }


    @OperationLogAnnotation(actionModule = "dataStatistics", actionType = "查看", actionUrl = "/admins/meeting/startDate")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看未结束会议的开始时间")
    @GetMapping("/meeting/startDate")
    public JsonResult selectStartDateTime() {
        log.info("get:/meeting/startDate 查看未结束会议的开始时间。");
        return meetingService.selectStartDateTime();
    }

    @OperationLogAnnotation(actionModule = "dataStatistics", actionType = "查看", actionUrl = "/meeting/chatJieba")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看所有聊天内容的分词结果")
    @GetMapping("/meeting/chatJieba")
    public JsonResult selectChatJieba() {
        log.info("get:/meeting/chatJieba 查看所有聊天内容(包括好友聊天)的分词结果。");
        return meetingChatService.selectChatJieba();
    }


    @OperationLogAnnotation(actionModule = "dataStatistics", actionType = "查看", actionUrl = "/meeting/chatJieba/stopword")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看分词的停用词")
    @GetMapping("/meeting/chatJieba/stopword")
    public JsonResult selectStopword() {
        log.info("get:/meeting/chatJieba/stopword 查看分词的停用词");
        return meetingChatService.selectStopword();
    }


    @OperationLogAnnotation(actionModule = "dataStatistics", actionType = "编辑", actionUrl = "/meeting/chatJieba/stopword")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("修改分词的停用词")
    @PutMapping("/meeting/chatJieba/stopword")
    public JsonResult updateStopword(@RequestBody List<String> words) {
        log.info("get:/meeting/chatJieba/stopword 修改分词的停用词");
        return meetingChatService.updateStopword(words);
    }


    @OperationLogAnnotation(actionModule = "meeting", actionType = "查看", actionUrl = "/meeting/#{#mid}/password")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("管理员获取mid对应会议的入会密码")
    @GetMapping("/meeting/{mid}/password")
    public JsonResult getMeetingPasswordById(@PathVariable String mid) {
        log.info(String.format("get:/meeting/%s/password 管理员获取mid对应会议的入会密码。", mid));
        return meetingService.getMeetingPasswordByIdAdmin(mid);
    }
}

