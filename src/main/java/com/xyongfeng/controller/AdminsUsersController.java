package com.xyongfeng.controller;

import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;


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
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("分页查看用户列表")
    @PostMapping("/userslist")
    public JsonResult select(@RequestBody @Validated MyPage myPage) {
        log.info(String.format("get:/users 查看用户列表。%s", myPage));
        List<Users> list = usersService.listPage(myPage);
        if (list.size() == 0) {
            return JsonResult.error("查询失败，页码超过已有大小");
        }
        return JsonResult.success("查询成功", list);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("添加用户")
    @PostMapping("/users")
    public JsonResult add(@RequestBody @Validated UsersAddParam users) {
        log.info(String.format("post:/users 添加用户。%s", users));
        try {
            usersService.userAdd(users,passwordEncoder);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("添加成功", users);
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("修改用户")
    @PutMapping("/users")
    public JsonResult update(@RequestBody @Validated UsersUpdateParam users) {
        log.info(String.format("put:/users 修改用户。%s", users));

        if (usersService.userUpdateById(users) > 0) {
            return JsonResult.error("修改成功", users);
        } else {
            return JsonResult.error("修改失败");
        }

    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("删除用户")
    @DeleteMapping("/users")
    public JsonResult delete(@RequestBody @Validated IDParam id) {
        log.info(String.format("delete:/users 删除用户。%s", id));
        Users delAdmin = usersService.userDelById(id.getId());
        if (delAdmin != null) {
            return JsonResult.success("删除成功", delAdmin);
        } else {
            return JsonResult.error("删除失败");
        }
    }

    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("设置用户为管理员")
    @PutMapping("/users/admin")
    public JsonResult setAdmin(@RequestBody @Validated UsersSetAdminParam param) {
        log.info(String.format("Put:/users/admin 设置用户为管理员。%s", param));
        return usersService.setAdmin(param);
    }
    @PreAuthorize("@SGExpressionRoot.hasAuthority('sys::user')")
    @ApiOperation("设置用户头像")
    @PostMapping("/users/headImg")
    public JsonResult setHeadImg(@Validated UsersSetImgParam param) throws IOException {
        log.info(String.format("Put:/users/headImg 设置用户头像。%s", param));

        return usersService.setHeadImg(param);
    }
}
