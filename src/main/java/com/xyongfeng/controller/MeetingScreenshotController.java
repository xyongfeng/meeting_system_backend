package com.xyongfeng.controller;


import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.ImgBase64Param;
import com.xyongfeng.service.MeetingScreenshotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-31
 */
@Api(tags = "会议截屏")
@Slf4j
@RestController
@RequestMapping("/meetingScreenshot")
public class MeetingScreenshotController {
    @Autowired
    private MeetingScreenshotService meetingScreenshotService;
    @ApiOperation("接收base64,保存截屏图像")
    @PostMapping("/{mid}")
    public JsonResult setScreenshotWithBase64(@RequestBody @Validated ImgBase64Param param, @PathVariable String mid) {
        log.info(String.format("Post:/meetingScreenshot/%s 接收base64,保存截屏图像 %s", mid, param));
        return meetingScreenshotService.setScreenshotWithBase64(mid, param);
    }
    @ApiOperation("根据会议id输出该用户的截屏路径")
    @GetMapping("/{mid}/path")
    public JsonResult selectPath( @PathVariable String mid) {
        log.info(String.format("Get:/meetingScreenshot/%s 接收base64,保存截屏图像", mid));
        return meetingScreenshotService.selectPath(mid);
    }

    @ApiOperation("输出该用户所有会议截屏")
    @GetMapping()
    public JsonResult selectAll() {
        log.info("Get:/meetingScreenshot 输出该用户所有会议截屏");
        return meetingScreenshotService.selectAll();
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/{id}")
    public JsonResult deleteById(@PathVariable String id) {
        log.info(String.format("Delete:/meetingScreenshot/%s 根据id删除", id));
        return meetingScreenshotService.deleteById(id);
    }
}

