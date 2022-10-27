package com.xyongfeng.controller;


import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

@Api(tags = "管理员操作meeting接口")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsMeetingController {
    @Autowired
    private MeetingService meetingService;


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("分页查看会议列表")
    @GetMapping("/meeting/{current}/{size}")
    public JsonResult select(@PathVariable Integer current,@PathVariable Integer size) {
        log.info(String.format("get:/meeting 查看会议列表。%d , %d", current,size));
        return meetingService.select(new PageParam(current,size));
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
        return meetingService.update(meeting);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("删除会议")
    @DeleteMapping("/meeting/{mid}")
    public JsonResult delete(@PathVariable String mid) {
        log.info(String.format("delete:/meeting 删除会议。%s", mid));
        return meetingService.delete(mid);
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('meeting')")
    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/{mid}/licence/{haveLicence}")
    public JsonResult setLicence(@PathVariable String mid, @PathVariable Integer haveLicence) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s %d", mid,haveLicence));
        return meetingService.setLicence(new MeetSetLicenceParam(mid,haveLicence == 1));
    }

}

