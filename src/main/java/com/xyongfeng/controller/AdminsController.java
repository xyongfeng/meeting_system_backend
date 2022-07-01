package com.xyongfeng.controller;

import com.xyongfeng.pojo.AdminsLoginParam;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.JwtTokenUtil;
import com.xyongfeng.util.ValidGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "操作用户接口", description = "进行管理员的操作")
@RestController
@Slf4j
@RequestMapping("/admins")
public class AdminsController {
    @Resource
    private UsersService usersService;
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
    public JsonResult login(@RequestBody @Validated AdminsLoginParam users, HttpServletRequest request
    ) {
        log.info(String.format("post:/adminLogin，进行登录,%s", users));
        // 判断验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        log.info(captcha);
        if (captcha == null || !captcha.equals(users.getCode())) {
            return JsonResult.error("验证码输入错误");
        }

        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(users.getUsername());
        if (null == userDetails || !passwordEncoder.matches(users.getPassword(), userDetails.getPassword())) {

            return JsonResult.error("用户名或密码错误");
        }
        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return JsonResult.success("登录成功", tokenMap);
    }

    @ApiOperation(value = "获取当前登录管理员信息")
    @GetMapping("/info")
    public JsonResult getAdminInfo(Principal principal) {
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        Users users = usersService.getUserByUserName(username);
        users.setPassword(null);
        return JsonResult.success("获取成功", users);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public JsonResult logout() {
        return JsonResult.success("退出成功");
    }

    @PreAuthorize("hasAuthority('sys::user')")
    @ApiOperation("分页查看管理员列表")
    @GetMapping("/admin")
    public JsonResult select(@RequestBody @Validated MyPage myPage) {
        log.info(String.format("get:/admin 查看管理员列表。%s", myPage));
        List<Users> list = usersService.listPage(myPage);
        if (list.size() == 0) {
            return JsonResult.error("查询失败，页码超过已有大小");
        }
        return JsonResult.success("查询成功", list);
    }

    @PreAuthorize("hasAuthority('sys::user')")
    @ApiOperation("添加管理员")
    @PostMapping("/admin")
    public JsonResult add(@RequestBody @Validated Users users) {
        log.info(String.format("post:/admin 添加管理员。%s", users));
        try {
            usersService.userAdd(users);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("添加成功", users);
    }

    @PreAuthorize("hasAuthority('sys::user')")
    @ApiOperation("修改管理员")
    @PutMapping("/admin")
    public JsonResult update(@RequestBody @Validated Users users) {
        log.info(String.format("put:/admin 修改管理员。%s", users));

        if (usersService.userUpdateById(users) > 0) {
            return JsonResult.error("修改成功", users);
        } else {
            return JsonResult.error("修改失败");
        }

    }

    @PreAuthorize("hasAuthority('sys::user')")
    @ApiOperation("删除管理员")
    @DeleteMapping("/admin")
    public JsonResult delete(@RequestBody @Validated Users users) {
        log.info(String.format("delete:/admin 删除管理员。%s", users));
        Users delAdmin = usersService.userDelById(users.getId());
        if (delAdmin != null) {
            return JsonResult.success("删除成功", delAdmin);
        } else {
            return JsonResult.error("删除失败");
        }

    }

}
