package com.xyongfeng.controller;

import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.User;
import com.xyongfeng.service.UserService;
import com.xyongfeng.util.JsonResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyongfeng
 */
@Api(tags = "操作用户接口",description = "进行用户表的操作")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public JsonResult<List<User>> getAll(){

        return new JsonResult<>(userService.getAll());
    }


}
