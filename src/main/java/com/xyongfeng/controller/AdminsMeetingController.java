package com.xyongfeng.controller;


import com.alibaba.fastjson.JSONObject;
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
    private AdminLogService adminLogService;
    @Autowired
    private MeetingChatService meetingChatService;


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("分页查看会议列表")
    @GetMapping("/meeting/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/meeting 查看会议列表。%d , %d", current, size));

        JsonResult jsonResult = meetingService.select(new PageParam(current, size));
        adminLogService.insert("meeting", "查看", "/admins/meeting", jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("添加会议")
    @PostMapping("/meeting")
    public JsonResult add(@RequestBody @Validated MeetingAddParam meeting) {
        log.info(String.format("post:/meeting 添加会议。%s", meeting));
        return meetingService.add(meeting);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("修改会议")
    @PutMapping("/meeting/{mid}")
    public JsonResult update(@RequestBody @Validated MeetingUpdateParam meeting, @PathVariable String mid) {
        meeting.setId(mid);
        log.info(String.format("put:/meeting 修改会议。%s", meeting));
        JsonResult jsonResult = meetingService.update(meeting);
        adminLogService.insert("meeting", "编辑", String.format("/admins/meeting/%s", mid), JSONObject.toJSONString(meeting), jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("删除会议")
    @DeleteMapping("/meeting/{mid}")
    public JsonResult delete(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 删除会议。%s", mid));
        JsonResult jsonResult = meetingService.delete(mid);
        adminLogService.insert("meeting", "删除", String.format("/admins/meeting/%s", mid), jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/{mid}/licence/{haveLicence}")
    public JsonResult setLicence(@PathVariable String mid, @PathVariable Integer haveLicence) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s %d", mid, haveLicence));
        JsonResult jsonResult = meetingService.setLicence(new MeetSetLicenceParam(mid, haveLicence == 1));
        adminLogService.insert("meeting", "编辑", String.format("/admins/meeting/%s/licence/%s", mid, haveLicence), jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看所有会议的开始时间")
    @GetMapping("/meeting/allStartDate")
    public JsonResult selectAllStartDateTime() {
        log.info("get:/meeting/allStartDate 查看所有会议的开始时间。");
        JsonResult jsonResult = meetingService.selectAllStartDateTime();
        adminLogService.insert("dataStatistics", "查看", "/admins/meeting/allStartDate", jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看未结束会议的开始时间")
    @GetMapping("/meeting/startDate")
    public JsonResult selectStartDateTime() {
        log.info("get:/meeting/startDate/noEnd 查看未结束会议的开始时间。");
        JsonResult jsonResult = meetingService.selectStartDateTime();
        adminLogService.insert("dataStatistics", "查看", "/admins/meeting/startDate/noEnd", jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看所有聊天内容的分词结果")
    @GetMapping("/meeting/chatJieba")
    public JsonResult selectChatJieba() {
        log.info("get:/meeting/chatJieba 查看所有聊天内容(包括好友聊天)的分词结果。");
        JsonResult jsonResult = meetingChatService.selectChatJieba();
        adminLogService.insert("dataStatistics", "查看", "/meeting/chatJieba", jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("查看分词的停用词")
    @GetMapping("/meeting/chatJieba/stopword")
    public JsonResult selectStopword() {
        log.info("get:/meeting/chatJieba/stopword 查看分词的停用词");
        JsonResult jsonResult = meetingChatService.selectStopword();
        adminLogService.insert("dataStatistics", "查看", "/meeting/chatJieba/stopword", jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('dataStatistics')")
    @ApiOperation("修改分词的停用词")
    @PutMapping("/meeting/chatJieba/stopword")
    public JsonResult updateStopword(@RequestBody List<String> words) {
        log.info("get:/meeting/chatJieba/stopword 修改分词的停用词");
        JsonResult jsonResult = meetingChatService.updateStopword(words);
        adminLogService.insert("dataStatistics", "编辑", "/meeting/chatJieba/stopword", jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("管理员获取mid对应会议的入会密码")
    @GetMapping("/meeting/{mid}/password")
    public JsonResult getMeetingPasswordById(@PathVariable String mid) {
        log.info(String.format("get:/meeting/%s/password 管理员获取mid对应会议的入会密码。", mid));
        JsonResult jsonResult = meetingService.getMeetingPasswordByIdAdmin(mid);
        adminLogService.insert("meeting", "查看", String.format("/meeting/%s/password", mid), jsonResult.getCode().equals(200));
        return jsonResult;
    }
}

