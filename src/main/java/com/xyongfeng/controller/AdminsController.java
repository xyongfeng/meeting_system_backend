package com.xyongfeng.controller;

import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.AdminsLoginParam;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.service.AdminsService;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.util.JwtTokenUtil;
import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody @Validated AdminsLoginParam admins, HttpServletRequest request) {
        log.info(String.format("post:/adminLogin，进行登录.账号为%s,密码为%s", admins.getUsername(), admins.getPassword()));
        // 判断验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(!captcha.equals(admins.getCode())){
            return JsonResult.error("验证码输入错误");
        }

        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(admins.getUsername());

        if (null == userDetails || ! passwordEncoder.matches(admins.getPassword(), userDetails.getPassword())){

            return JsonResult.error("用户名或密码错误");
        }
        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return JsonResult.success("登录成功",tokenMap);
    }
    @ApiOperation(value = "获取当前登录管理员信息")
    @GetMapping("/info")
    public JsonResult getAdminInfo(Principal principal){
        if (principal == null){
            return null;
        }
        String username = principal.getName();
        Admins admins = adminsService.getAdminByUserName(username);
        admins.setPassword(null);
        return JsonResult.success("获取成功",admins);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public JsonResult logout(){
        return JsonResult.success("退出成功");
    }

    @ApiOperation("分页查看管理员列表")
    @GetMapping("/admin")
    public JsonResult select(@RequestBody @Validated MyPage myPage) {
        log.info(String.format("get:/admin 查看管理员列表。%s", myPage));
        List<Admins> list = adminsService.listPage(myPage);
        if (list.size() == 0) {
            return new JsonResult(HttpStatus.BAD_REQUEST.value(), "查询失败，页码超过已有大小");
        }
        return new JsonResult(list, HttpStatus.OK.value(), "查询成功");
    }

    @ApiOperation("添加管理员")
    @PostMapping("/admin")
    public JsonResult add(@RequestBody @Validated(value = {ValidGroups.Default.class, ValidGroups.Update.class}) Admins admins) {
        log.info(String.format("post:/admin 添加管理员。%s", admins));
        try {
            adminsService.adminAdd(admins);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new JsonResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new JsonResult(admins, HttpStatus.OK.value(), "添加成功");
    }

    @ApiOperation("修改管理员")
    @PutMapping("/admin")
    public JsonResult update(@RequestBody @Validated(value =
            {ValidGroups.Default.class, ValidGroups.Update.class, ValidGroups.Id.class}) Admins admins) {
        log.info(String.format("put:/admin 修改管理员。%s", admins));

        if (adminsService.adminUpdateById(admins) > 0) {
            return new JsonResult(admins, HttpStatus.OK.value(), "修改成功");
        } else {
            return new JsonResult(HttpStatus.OK.value(), "修改失败");
        }

    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/admin")
    public JsonResult delete(@RequestBody @Validated(value =
            {ValidGroups.Id.class}) Admins admins) {
        log.info(String.format("delete:/admin 删除管理员。%s", admins));
        Admins delAdmin = adminsService.adminDelById(admins.getId());
        if (delAdmin != null) {
            return new JsonResult(delAdmin, HttpStatus.OK.value(), "删除成功");
        } else {
            return new JsonResult(HttpStatus.OK.value(), "删除失败");
        }

    }

}
