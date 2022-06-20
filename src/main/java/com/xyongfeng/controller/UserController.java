package com.xyongfeng.controller;

import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.UserService;
import com.xyongfeng.util.JsonResult;
import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyongfeng
 */
@Api(tags = "操作用户接口", description = "进行用户表的操作")
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public JsonResult<Users> login(@RequestBody @Validated(value = {ValidGroups.Default.class}) Users users) {
        log.info(String.format("post:/login，进行登录.账号为%s,密码为%s", users.getUsername(), users.getPassword()));
        Users user = userService.userLogin(users.getUsername(), users.getPassword());
        if (user != null) {
            log.info("登录成功");
            return new JsonResult<>(user, HttpStatus.OK.value(), "登录成功");
        } else {
            log.info("登录失败");
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), "登录失败");
        }
    }

    @ApiOperation("分页查看用户列表")
    @GetMapping("/user")
    public JsonResult<List<Users>> select(@RequestBody @Validated MyPage myPage) {
        log.info(String.format("get:/user 查看用户列表。%s", myPage));
        List<Users> list = userService.listPage(myPage);
        if (list.size() == 0) {
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), "查询失败，页码超过已有大小");
        }
        return new JsonResult<>(list, HttpStatus.OK.value(), "查询成功");
    }

    @ApiOperation("添加用户")
    @PostMapping("/user")
    public JsonResult<Users> add(@RequestBody @Validated(value = {ValidGroups.Default.class, ValidGroups.Update.class}) Users users) {
        log.info(String.format("post:/user 添加用户。%s", users));
        try {
            userService.userAdd(users);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new JsonResult<>(users, HttpStatus.OK.value(), "添加成功");
    }

    @ApiOperation("修改用户")
    @PutMapping("/user")
    public JsonResult<Users> update(@RequestBody @Validated(value =
            {ValidGroups.Default.class, ValidGroups.Update.class, ValidGroups.Id.class}) Users users) {
        log.info(String.format("put:/user 修改用户。%s", users));

        if (userService.userUpdateById(users) > 0) {
            return new JsonResult<>(users, HttpStatus.OK.value(), "修改成功");
        } else {
            return new JsonResult<>(HttpStatus.OK.value(), "修改失败");
        }

    }

    @ApiOperation("删除用户")
    @DeleteMapping("/user")
    public JsonResult<Users> delete(@RequestBody @Validated(value =
            {ValidGroups.Id.class}) Users users) {
        log.info(String.format("delete:/user 删除用户。%s", users));
        Users delUser = userService.userDelById(users.getId());
        if (delUser != null) {
            return new JsonResult<>(delUser, HttpStatus.OK.value(), "删除成功");
        } else {
            return new JsonResult<>(HttpStatus.OK.value(), "删除失败");
        }

    }


}
