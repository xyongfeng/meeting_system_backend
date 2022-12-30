package com.xyongfeng.controller;


import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.pojo.ChatFilter;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.ChatFilterParam;
import com.xyongfeng.pojo.UserAdvice;
import com.xyongfeng.service.AdminLogService;
import com.xyongfeng.service.UserAdviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2022-12-29
 */
@Slf4j
@Api(tags = "管理用户意见")
@RestController
@RequestMapping("/userAdvice")
public class UserAdviceController {

    @Autowired
    private UserAdviceService userAdviceService;

    @Autowired
    private AdminLogService adminLogService;

    @PreAuthorize("@SGExpressionRoot.hasAuthority('userAdvice')")
    @ApiOperation("分页查看意见列表")
    @GetMapping("/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/userAdvice 分页查看意见列表。%d , %d", current, size));
        JsonResult jsonResult = userAdviceService.select(current, size);
        adminLogService.insert("userAdvice", "查看", "/userAdvice", jsonResult.getCode().equals(200));
        return jsonResult;
    }


    @ApiOperation("添加意见")
    @PostMapping()
    public JsonResult insert(@RequestBody @Validated UserAdvice userAdvice) {
        log.info(String.format("post:/userAdvice 添加意见。%s", userAdvice));
        return userAdviceService.insert(userAdvice);

    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('userAdvice')")
    @ApiOperation("删除意见")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable Integer id) {
        log.info(String.format("delete:/userAdvice 删除意见。%s", id));
        JsonResult jsonResult = userAdviceService.delete(id);

        adminLogService.insert("userAdvice", "删除", String.format("/userAdvice/%s", id), jsonResult.getCode().equals(200));
        return jsonResult;
    }
}

