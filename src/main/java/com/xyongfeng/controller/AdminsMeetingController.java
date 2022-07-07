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


    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::meeting')")
    @ApiOperation("分页查看会议列表")
    @PostMapping("/meetinglist")
    public JsonResult select(@RequestBody @Validated PageParam pageParam) {
        log.info(String.format("get:/meeting 查看会议列表。%s", pageParam));
        return meetingService.select(pageParam);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::meeting')")
    @ApiOperation("添加会议")
    @PostMapping("/meeting")
    public JsonResult add(@RequestBody @Validated MeetingAddParam meeting) {
        log.info(String.format("post:/meeting 添加会议。%s", meeting));
        return meetingService.add(meeting);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::meeting')")
    @ApiOperation("修改会议")
    @PutMapping("/meeting")
    public JsonResult update(@RequestBody @Validated MeetingUpdateParam meeting) {
        log.info(String.format("put:/meeting 修改会议。%s", meeting));
        return meetingService.update(meeting);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::meeting')")
    @ApiOperation("删除会议")
    @DeleteMapping("/meeting")
    public JsonResult delete(@RequestBody @Validated LongIDParam id) {
        log.info(String.format("delete:/meeting 删除会议。%s", id));
        return meetingService.delete(id);
    }


    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::meeting')")
    @ApiOperation("会议是否需要创建者许可才能进入")
    @PutMapping("/meeting/licence")
    public JsonResult setLicence(@RequestBody @Validated MeetSetLicenceParam param) {
        log.info(String.format("Put:/meeting/licence 会议是否需要创建者许可才能进入。%s", param));
        return meetingService.setLicence(param);
    }

}

