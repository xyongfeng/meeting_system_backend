package com.xyongfeng.controller;


import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
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
 * @since 2022-06-25
 */
@RestController
@RequestMapping("/role")
@Api(tags = "权限")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;


    @PreAuthorize("@SGExpressionRoot.hasAuthority('role')")
    @ApiOperation("查看管理员权限列表(隐藏权限除外)")
    @GetMapping()
    public JsonResult getAllRole() {
        log.info("Get:/role 查看管理员权限列表。");
        return roleService.selectAllRole();
    }


}

