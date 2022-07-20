package com.xyongfeng.controller;


import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
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
    @PostMapping("/meetingList")
    public JsonResult selectByCreated(@RequestBody @Validated PageParam pageParam) {
        log.info(String.format("post:/meetingList 分页查看自己创建的会议列表。%s", pageParam));
        return meetingService.selectByUser(pageParam);
    }

    @ApiOperation("分页查看自己加入的会议列表")
    @PostMapping("/joinedMeetingList")
    public JsonResult selectByJoined(@RequestBody @Validated PageParam pageParam) {
        log.info(String.format("post:/joinedMeetingList 分页查看自己加入的会议列表。%s", pageParam));
        return meetingService.selectByUserJoined(pageParam);
    }

    @ApiOperation("添加会议")
    @PostMapping("/meeting")
    public JsonResult add(@RequestBody @Validated MeetingAddParam meeting) {
        log.info(String.format("post:/meeting 添加会议。%s", meeting));
        return meetingService.addByUser(meeting);
    }

    @ApiOperation("修改会议")
    @PutMapping("/meeting")
    public JsonResult update(@RequestBody @Validated MeetingUpdateParam meeting) {
        log.info(String.format("put:/meeting 修改会议。%s", meeting));
        return meetingService.updateByUser(meeting);
    }

    @ApiOperation("删除会议")
    @DeleteMapping("/meeting")
    public JsonResult delete(@RequestBody @Validated LongIDParam id) {
        log.info(String.format("delete:/meeting 删除会议。%s", id));
        return meetingService.deleteByUser(id);
    }

    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/licence")
    public JsonResult setLicence(@RequestBody @Validated MeetSetLicenceParam param) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s", param));
        return meetingService.setLicenceByUser(param);
    }

    @ApiOperation("加入会议")
    @PostMapping("/joinMeeting")
    public JsonResult joinMeeting(@RequestBody @Validated LongIDParam id) {
        log.info(String.format("delete:/meeting 加入会议。%s", id));
        return meetingService.joinMeeting(id);
    }
    @ApiOperation("退出会议")
    @DeleteMapping("/joinMeeting")
    public JsonResult outMeeting(@RequestBody @Validated LongIDParam id) {
        log.info(String.format("delete:/meeting 退出会议。%s", id));
        return meetingService.outMeeting(id);
    }
}

