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
    @GetMapping("/meeting/{current}/{size}")
    public JsonResult selectByCreated(@PathVariable Integer current,@PathVariable Integer size) {
        log.info(String.format("post:/meetingList 分页查看自己创建的会议列表。%d , %d", current,size));
        return meetingService.selectByUser(new PageParam(current,size));
    }

    @ApiOperation("分页查看自己加入的会议列表")
    @GetMapping("/meeting/joined/{current}/{size}")
    public JsonResult selectByJoined(@PathVariable Integer current,@PathVariable Integer size) {
        log.info(String.format("post:/joinedMeetingList 分页查看自己加入的会议列表。%d , %d", current,size));
        return meetingService.selectByUserJoined(new PageParam(current,size));
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
    public JsonResult delete( @PathVariable String mid) {
        log.info(String.format("delete:/meeting 删除会议。%s", mid));
        return meetingService.deleteByUser(new LongIDParam(mid));
    }

    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/{mid}/licence/{haveLicence}")
    public JsonResult setLicence(@PathVariable String mid, @PathVariable Integer haveLicence) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s %d", mid,haveLicence));
        return meetingService.setLicenceByUser(new MeetSetLicenceParam(mid,haveLicence == 1));
    }

    @ApiOperation("加入会议")
    @PostMapping("/joinMeeting/{mid}")
    public JsonResult joinMeeting( @PathVariable String mid) {
        log.info(String.format("delete:/meeting 加入会议。%s", mid));
        return meetingService.joinMeeting(new LongIDParam(mid));
    }
    @ApiOperation("退出会议")
    @DeleteMapping("/joinMeeting/{mid}")
    public JsonResult outMeeting(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 退出会议。%s", mid));
        return meetingService.outMeeting(new LongIDParam(mid));
    }

    @ApiOperation("通过id查询自己参与的会议的信息")
    @GetMapping("/meeting/{id}")
    public JsonResult getMeeting(@PathVariable Long id) {
        log.info(String.format("delete:/meeting 通过id查询自己参与的会议的信息。%s", id));
        return meetingService.getMeetingById(id,false);
    }
}

