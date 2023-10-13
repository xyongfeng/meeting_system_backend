package com.xyongfeng.controller;

import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.aop.OperationLogAnnotation;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.AdminLogService;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author xyongfeng
 */
@Api(tags = "管理员操作users接口", description = "进行users的操作")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsUsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @OperationLogAnnotation(actionModule = "user", actionType = "查看", actionUrl = "/admins/users/#{#current}/#{#size}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("分页查看用户列表")
    @GetMapping("/users/{current}/{size}")
    public JsonResult select(@PathVariable Integer current, @PathVariable Integer size) {
        log.info(String.format("get:/users 查看用户列表。%d,%d", current, size));
        return usersService.select(current, size);
    }


    @OperationLogAnnotation(actionModule = "user", actionType = "新增", actionUrl = "/admins/users", actionContent = "#{#users}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("添加用户")
    @PostMapping("/users")
    public JsonResult add(@RequestBody @Validated UsersAddParam users) {
        log.info(String.format("post:/users 添加用户。%s", users));

        return usersService.add(users, passwordEncoder);
    }


    @OperationLogAnnotation(actionModule = "user", actionType = "编辑", actionUrl = "/admins/users/#{#uid}", actionContent = "#{#users}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("修改用户")
    @PutMapping("/users/{uid}")
    public JsonResult update(@RequestBody @Validated UsersUpdateParam users, @PathVariable Integer uid) {
        log.info(String.format("put:/users 修改用户。%s", users));
        return usersService.update(users, uid, passwordEncoder);
    }

    @OperationLogAnnotation(actionModule = "user", actionType = "删除", actionUrl = "/admins/users/#{#uid}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("删除用户")
    @DeleteMapping("/users/{uid}")
    public JsonResult delete(@PathVariable Integer uid) {
        log.info(String.format("delete:/users 删除用户。%s", uid));
        return usersService.delete(uid);
    }


    @OperationLogAnnotation(actionModule = "user", actionType = "编辑", actionUrl = "/admins/users/#{#uid}/admin/#{#isAdmin}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("设置用户是否为管理员")
    @PutMapping("/users/{uid}/admin/{isAdmin}")
    public JsonResult setAdmin(@PathVariable Integer uid, @PathVariable Integer isAdmin) {
        log.info(String.format("Put:/users/admin 设置用户为管理员。%d,%d", uid, isAdmin));
        return usersService.setAdmin(new UsersSetAdminParam(uid, isAdmin == 1));
    }

    @OperationLogAnnotation(actionModule = "user", actionType = "编辑", actionUrl = "/admins/users/#{#uid}/headImg")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('user')")
    @ApiOperation("设置用户头像")
    @PutMapping("/users/{uid}/headImg")
    public JsonResult setHeadImg(@Validated UsersSetImgParam param, @PathVariable Integer uid) {
        param.setId(uid);
        log.info(String.format("Put:/users/{uid}/headImg 设置用户头像。%s", param));
        return usersService.setHeadImg(param);
    }

    @OperationLogAnnotation(actionModule = "user", actionType = "查看", actionUrl = "/admins/users/#{#uid}/role")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('role')")
    @ApiOperation("查看管理员拥有的权限")
    @GetMapping("/users/{uid}/role")
    public JsonResult getRoleWithoutHidden(@PathVariable Integer uid) {
        log.info(String.format("Get:/users/{uid}/role 查看管理员权限。%d", uid));
        return roleService.selectRoleWithoutHidden(uid);
    }

    @OperationLogAnnotation(actionModule = "user", actionType = "编辑", actionUrl = "/admins/users/#{#uid}/role", actionContent = "#{#roleParam}")
    @PreAuthorize("@SGExpressionRoot.hasAuthority('role')")
    @ApiOperation("修改管理员权限")
    @PutMapping("/users/{uid}/role")
    public JsonResult setRole(@PathVariable Integer uid, @RequestBody Map<Integer, Boolean> roleParam) {
        log.info(String.format("Get:/users/{uid}/authority 修改管理员权限。%d %s", uid, roleParam));
        return roleService.updateRole(uid, roleParam);
    }

}
