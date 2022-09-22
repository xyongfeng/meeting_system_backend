package com.xyongfeng.controller;


import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */

@Api(tags = "用户操作meeting接口")
@RestController
@Slf4j
@RequestMapping("/users")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @ApiOperation("分页查看自己创建的会议列表")
    @GetMapping("/meeting/{current}/{size}")
    public JsonResult selectByCreated(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("post:/meetingList 分页查看自己创建的会议列表。%d , %d", current, size));
        return meetingService.selectByUser(new PageParam(current, size));
    }

    @ApiOperation("分页查看自己加入的会议列表")
    @GetMapping("/meeting/joined/{current}/{size}")
    public JsonResult selectByJoined(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/joinedMeetingList 分页查看自己加入的会议列表。%d , %d", current, size));
        return meetingService.selectByUserJoined(new PageParam(current, size));
    }

    @ApiOperation("添加会议")
    @PostMapping("/meeting")
    public JsonResult add(@RequestBody @Validated MeetingAddParam meeting) {
        log.info(String.format("post:/meeting 添加会议。%s", meeting));
        return meetingService.addByUser(meeting);
    }

    @ApiOperation("修改会议")
    @PutMapping("/meeting/{mid}")
    public JsonResult update(@RequestBody @Validated MeetingUpdateParam meeting, @PathVariable String mid) {
        meeting.setId(mid);
        log.info(String.format("put:/meeting 修改会议。%s", meeting));
        return meetingService.updateByUser(meeting);
    }

    @ApiOperation("删除会议")
    @DeleteMapping("/meeting/{mid}")
    public JsonResult delete(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 删除会议。%s", mid));
        return meetingService.deleteByUser(mid);
    }

    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/{mid}/licence/{haveLicence}")
    public JsonResult setLicence(@PathVariable String mid, @PathVariable Integer haveLicence) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s %d", mid, haveLicence));
        return meetingService.setLicenceByUser(new MeetSetLicenceParam(mid, haveLicence == 1));
    }

    @ApiOperation("加入会议")
    @PostMapping("/meeting/joined/{mid}")
    public JsonResult joinMeeting(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 加入会议。%s", mid));
        return meetingService.joinMeeting(mid);
    }

    @ApiOperation("退出会议")
    @DeleteMapping("/meeting/joined/{mid}")
    public JsonResult outMeeting(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 退出会议。%s", mid));
        return meetingService.outMeeting(mid);
    }

    @ApiOperation("通过id查询自己参与的会议的信息")
    @GetMapping("/meeting/{mid}")
    public JsonResult getMeeting(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 通过id查询自己参与的会议的信息。%s", mid));
        return meetingService.getMeetingById(mid, false);
    }

    @ApiOperation("分页查看自己会议的用户签到列表")
    @GetMapping("/meeting/{mid}/hadSignIn/{current}/{size}")
    public JsonResult getHadSignInList(@PathVariable Integer current, @PathVariable Integer size, @PathVariable String mid) {
        log.info(String.format("get:/getHadSignInList 分页查看自己会议的用户签到列表。%d , %d", current, size));
        return meetingService.selectHadSignInList(mid, current, size);
    }

    @ApiOperation("分页查看自己会议的申请通知（未读）")
    @GetMapping("/meeting/application/{current}/{size}")
    public JsonResult getMeetingApplications(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/getMeetingApplications 分页查看自己会议的申请通知。%d , %d", current, size));
        return meetingService.selectMeetingApplications(current, size);
    }

    @ApiOperation(value = "回应会议申请")
    @PostMapping("/meeting/{meetingId}/application/{userId}/{result}")
    public JsonResult replyMeetingApplication(@PathVariable String meetingId, @PathVariable Integer userId, @PathVariable Integer result) {
        log.info(String.format("post:/replyMeetingApplication 回应会议申请。%s , %d , %d", meetingId, userId, result));
        return meetingService.replyMeetingApplication(meetingId, userId, result);
    }

    @ApiOperation("获得自己参加会议的通知列表(未读)")
    @GetMapping("/meeting/inform/{current}/{size}")
    public JsonResult getMeetingNoticePushWithUid(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/getMeetingNoticePushWithUid 获得自己参加会议的通知列表。%d , %d", current, size));
        return meetingService.selectMeetingNoticePush(current, size);
    }

    @ApiOperation("对会议通知进行已读")
    @PostMapping("/meeting/inform/{informId}")
    public JsonResult readMeetinInfrom(@PathVariable Integer informId) {
        log.info(String.format("post:/readMeetinInform 对会议通知进行已读。%d", informId));
        return meetingService.readMeetinInfrom(informId);
    }

    @ApiOperation("获取mid对应会议的公告列表")
    @GetMapping("/meeting/{mid}/notice/{current}/{size}")
    public JsonResult getMeetingNoticeById(@PathVariable String mid, @PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/getMeetingNoticeById 获取mid对应会议的公告列表。%s %d %d", mid, current, size));
        return meetingService.getMeetingNoticeById(mid, current, size);
    }

    @ApiOperation("为会议加入公告")
    @PostMapping("/meeting/{mid}/notice")
    public JsonResult addMeetingNotice(@PathVariable String mid, @RequestBody MeetingNotice meetingNotice) {
        log.info(String.format("post:/addMeetingNotice 为会议加入公告。%s", mid));
        return meetingService.addMeetingNotice(meetingNotice.setMeetingId(mid));
    }

    @ApiOperation("编辑会议公告")
    @PutMapping("/meeting/{mid}/notice")
    public JsonResult updateMeetingNotice(@PathVariable String mid, @RequestBody MeetingNotice meetingNotice) {
        log.info(String.format("update:/updateMeetingNotice 编辑会议公告。%s", mid));
        meetingNotice.setMeetingId(mid);
        return meetingService.updateMeetingNotice(meetingNotice);
    }

    @ApiOperation("删除指定id的公告")
    @DeleteMapping("/meeting/{mid}/notice/{noticeId}")
    public JsonResult delMeetingNoticeById(@PathVariable String mid, @PathVariable Integer noticeId) {
        log.info(String.format("delete:/delMeetingNoticeById 获取mid对应会议的公告列表。%s %d", mid, noticeId));
        return meetingService.delMeetingNoticeById(mid,noticeId);
    }


}

