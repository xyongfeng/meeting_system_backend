package com.xyongfeng.controller;


import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Menu;
import com.xyongfeng.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Api(tags = "管理员操作meeting接口")
@RestController
@RequestMapping("/admins/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "通过用户ID查询菜单列表")
    @GetMapping
    public JsonResult getMenusByAdminId(){
        return menuService.getMenusByUserId();
    }


}

