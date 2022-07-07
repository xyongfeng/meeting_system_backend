package com.xyongfeng.controller;


import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.LongIDParam;
import com.xyongfeng.pojo.Param.MeetingAddParam;
import com.xyongfeng.pojo.Param.MeetingUpdateParam;
import com.xyongfeng.pojo.Param.PageParam;
import com.xyongfeng.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @ApiOperation("分页查看会议列表")
    @PostMapping("/meetinglist")
    public JsonResult select(@RequestBody @Validated PageParam pageParam) {
        log.info(String.format("get:/meeting 查看会议列表。%s", pageParam));
        return meetingService.selectByUser(pageParam);
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




}

