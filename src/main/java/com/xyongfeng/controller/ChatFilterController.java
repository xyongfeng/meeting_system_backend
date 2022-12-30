package com.xyongfeng.controller;


import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.pojo.ChatFilter;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.AdminLogService;
import com.xyongfeng.service.ChatFilterService;
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
@Api(tags = "管理聊天过滤词")
@RestController
@RequestMapping("/chatFilter")
public class ChatFilterController {
    @Autowired
    private ChatFilterService chatFilterService;
    @Autowired
    private AdminLogService adminLogService;

    @PreAuthorize("@SGExpressionRoot.hasAuthority('chatFilter')")
    @ApiOperation("分页查看过滤词列表")
    @GetMapping("/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/chatFilter 分页查看过滤词列表。%d , %d", current, size));
        JsonResult jsonResult = chatFilterService.select(current, size);
        adminLogService.insert("chatFilter", "查看", "/chatFilter", jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('chatFilter')")
    @ApiOperation("添加过滤词")
    @PostMapping()
    public JsonResult insert(@RequestBody @Validated ChatFilter chatFilter) {
        log.info(String.format("post:/chatFilter 添加过滤词。%s", chatFilter));
        JsonResult jsonResult = chatFilterService.insert(chatFilter);
        ChatFilterParam chatFilterParam = new ChatFilterParam(chatFilter.getId(), chatFilter.getFilterContent(), chatFilter.getFilterRule(), chatFilter.getReplaceContent());
        adminLogService.insert("chatFilter", "新增", "/chatFilter", JSONObject.toJSONString(chatFilterParam), jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('chatFilter')")
    @ApiOperation("修改过滤词")
    @PutMapping("/{id}")
    public JsonResult update(@RequestBody @Validated ChatFilter chatFilter, @PathVariable Integer id) {
        chatFilter.setId(id);
        log.info(String.format("put:/chatFilter 修改过滤词。%s", chatFilter));
        JsonResult jsonResult = chatFilterService.updateOne(chatFilter);
        ChatFilterParam chatFilterParam = new ChatFilterParam(chatFilter.getId(), chatFilter.getFilterContent(), chatFilter.getFilterRule(), chatFilter.getReplaceContent());
        adminLogService.insert("chatFilter", "编辑", String.format("/chatFilter/%s", id), JSONObject.toJSONString(chatFilterParam), jsonResult.getCode().equals(200));

        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('chatFilter')")
    @ApiOperation("删除过滤词")
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable Integer id) {
        log.info(String.format("delete:/chatFilter 删除过滤词。%s", id));
        JsonResult jsonResult = chatFilterService.delete(id);

        adminLogService.insert("chatFilter", "删除", String.format("/chatFilter/%s", id), jsonResult.getCode().equals(200));
        return jsonResult;
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('chatFilter')")
    @ApiOperation("设置过滤词是否启用")
    @PutMapping("/{id}/enable/{enable}")
    public JsonResult setAdmin(@PathVariable Integer id, @PathVariable Integer enable) {
        log.info(String.format("Put:/chatFilter/{id}/enable/{enable} 设置过滤词是否启用。%d,%d", id, enable));
        JsonResult jsonResult = chatFilterService.setEnable(id, enable == 1);
        adminLogService.insert("chatFilter", "编辑", String.format("/chatFilter/%s/enable/%s", id, enable), jsonResult.getCode().equals(200));
        return jsonResult;
    }
}

