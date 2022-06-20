package com.xyongfeng.controller;

import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.service.AdminsService;
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
@Api(tags = "操作管理员接口", description = "进行管理员表的操作")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsController {
    @Resource
    private AdminsService adminsService;


    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public JsonResult<Admins> login(@RequestBody @Validated(value = {ValidGroups.Default.class}) Admins admins) {
        log.info(String.format("post:/adminLogin，进行登录.账号为%s,密码为%s", admins.getUsername(), admins.getPassword()));
        Admins admin = adminsService.adminLogin(admins.getUsername(), admins.getPassword());
        if (admin != null) {
            log.info("登录成功");
            return new JsonResult<>(admin, HttpStatus.OK.value(), "登录成功");
        } else {
            log.info("登录失败");
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), "登录失败");
        }
    }

    @ApiOperation("分页查看管理员列表")
    @GetMapping("/admin")
    public JsonResult<List<Admins>> select(@RequestBody @Validated MyPage myPage) {
        log.info(String.format("get:/admin 查看管理员列表。%s", myPage));
        List<Admins> list = adminsService.listPage(myPage);
        if (list.size() == 0) {
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), "查询失败，页码超过已有大小");
        }
        return new JsonResult<>(list, HttpStatus.OK.value(), "查询成功");
    }

    @ApiOperation("添加管理员")
    @PostMapping("/admin")
    public JsonResult<Admins> add(@RequestBody @Validated(value = {ValidGroups.Default.class, ValidGroups.Update.class}) Admins admins) {
        log.info(String.format("post:/admin 添加管理员。%s", admins));
        try {
            adminsService.adminAdd(admins);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new JsonResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new JsonResult<>(admins, HttpStatus.OK.value(), "添加成功");
    }

    @ApiOperation("修改管理员")
    @PutMapping("/admin")
    public JsonResult<Admins> update(@RequestBody @Validated(value =
            {ValidGroups.Default.class, ValidGroups.Update.class, ValidGroups.Id.class}) Admins admins) {
        log.info(String.format("put:/admin 修改管理员。%s", admins));

        if(adminsService.adminUpdateById(admins) > 0){
            return new JsonResult<>(admins, HttpStatus.OK.value(), "修改成功");
        }else{
            return new JsonResult<>(HttpStatus.OK.value(), "修改失败");
        }

    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/admin")
    public JsonResult<Admins> delete(@RequestBody @Validated(value =
            {ValidGroups.Id.class}) Admins admins) {
        log.info(String.format("delete:/admin 删除管理员。%s", admins));
        Admins delAdmin = adminsService.adminDelById(admins.getId());
        if(delAdmin != null){
            return new JsonResult<>(delAdmin, HttpStatus.OK.value(), "删除成功");
        }else{
            return new JsonResult<>(HttpStatus.OK.value(), "删除失败");
        }

    }

}
