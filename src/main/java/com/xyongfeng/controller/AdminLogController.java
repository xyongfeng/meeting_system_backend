package com.xyongfeng.controller;


import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.AdminLogService;
import com.xyongfeng.service.ChatFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Slf4j
@Api(tags = "管理员操作日志")
@RestController
@RequestMapping("/adminLog")
public class AdminLogController {
    @Autowired
    private AdminLogService adminLogService;

    @PreAuthorize("@SGExpressionRoot.hasAuthority('adminLog')")
    @ApiOperation("分页查看操作日志列表")
    @GetMapping("/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/adminLog 分页查看操作日志列表。%d , %d", current, size));
        return adminLogService.select(current, size);
    }
}

