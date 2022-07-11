package com.xyongfeng.controller;

import com.xyongfeng.pojo.Param.AdminsLoginParam;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.UsersRegisterParam;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;

@Api(tags = "普通用户user操作")
@RestController
@Slf4j
@RequestMapping("/users")
public class UsersController {
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UsersService usersService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody @Validated AdminsLoginParam users, HttpServletRequest request
    ) {
        log.info(String.format("post:/login，进行登录,%s", users));
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

    @ApiOperation(value = "获取当前登录的用户信息")
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

    @ApiOperation(value = "签到")
    @PostMapping("/register")
    public JsonResult register(@RequestBody @Validated UsersRegisterParam param) {
        return usersService.register(param);
    }

}
