package com.xyongfeng.controller;

import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.User;
import com.xyongfeng.service.AdminsService;
import com.xyongfeng.util.JsonResult;
import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.java.Log;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @author xyongfeng
 */
@Api(tags = "操作管理员接口",description = "进行管理员表的操作")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsController {
    @Resource
    private AdminsService adminsService;


    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public JsonResult<Admins> adminLogin(@RequestBody @Validated(value = {ValidGroups.Default.class}) Admins admins) {
        log.info(String.format("/adminLogin，进行登录.账号为%s,密码为%s", admins.getUsername(), admins.getPassword()));
        Admins admin = adminsService.adminLogin(admins.getUsername(), admins.getPassword());
        if (admin != null) {
            log.info("登录成功");
            return new JsonResult<>(admin,HttpStatus.OK.value(), "登录成功");
        } else {
            log.info("登录失败");
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), "登录失败");
        }
    }

}
