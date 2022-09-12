package com.xyongfeng.controller;

import com.xyongfeng.mapper.MeetingUsersMapper;
import com.xyongfeng.pojo.Param.AdminsLoginParam;
import com.xyongfeng.pojo.JsonResult;
//import com.xyongfeng.pojo.Param.UsersRegisterParam;
import com.xyongfeng.pojo.Param.UsersSignInParam;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(tags = "普通用户操作user接口及基本操作")
@RestController
@Slf4j
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
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
    @PostMapping("/signIn/{mid}")
    public JsonResult SignIn(@RequestBody @Validated UsersSignInParam param, @PathVariable String mid) {
        return usersService.signIn(param, mid);
    }

    @ApiOperation(value = "检查是否签到")
    @GetMapping("/signIn/{mid}")
    public JsonResult getSignIn(@PathVariable String mid) {
        return usersService.hadSignIn(mid);
    }

    @ApiOperation(value = "通过姓名获取该用户的信息")
    @GetMapping("/info/{userName}")
    public JsonResult getUserInfoByName(@PathVariable String userName) {
        return usersService.getUserInfoByName(userName);
    }

    @ApiOperation(value = "通过uid获取该用户的信息")
    @GetMapping("/info/{uid}/byId")
    public JsonResult getUserInfoById(@PathVariable Integer uid) {
        return usersService.getUserInfoById(uid);
    }

    @ApiOperation(value = "发送好友申请")
    @PostMapping("/friend/application/{userid}")
    public JsonResult sendFriApplication(@PathVariable Integer userid) {
        return usersService.sendFriApplication(userid);
    }

    @ApiOperation(value = "回应好友申请")
    @PostMapping("/friend/application/{userid}/{result}")
    public JsonResult replyFriApplication(@PathVariable Integer userid, @PathVariable Integer result) {
        return usersService.replyFriApplication(userid, result);
    }

    @ApiOperation(value = "查看好友申请列表（未读）")
    @GetMapping("/friend/application/{current}/{size}")
    public JsonResult getFriApplications(@PathVariable Integer current, @PathVariable Integer size) {
        return usersService.getFriApplications(current, size);
    }

    @ApiOperation(value = "根据userid删除好友")
    @DeleteMapping("/friend/{userid}")
    public JsonResult delFriendById(@PathVariable Integer userid) {
        return usersService.delFriendById(userid);
    }

    @ApiOperation(value = "查看好友列表以及聊天记录")
    @GetMapping("/friend/{current}/{size}")
    public JsonResult getFriendsAndChat(@PathVariable Integer current, @PathVariable Integer size) {
        return usersService.getFriendsAndChat(current, size);
    }

    @ApiOperation(value = "查看与好友聊天记录")
    @GetMapping("/friend/{userid}/chat/{current}/{size}")
    public JsonResult getFriChat(@PathVariable Integer userid, @PathVariable Integer current, @PathVariable Integer size) {
        return usersService.getFriChat(userid, current, size);
    }

    @ApiOperation(value = "向好友发送消息")
    @PostMapping("/friend/{userid}/chat")
    public JsonResult sendFriChat(@PathVariable Integer userid, @RequestBody Map map) {
        String content = (String) map.get("content");
        return usersService.sendFriChat(userid, content);
    }
    @ApiOperation(value = "对好友消息进行已读")
    @PutMapping("/friend/{userid}/chat")
    public JsonResult readFriChat(@PathVariable Integer userid) {

        return usersService.readFriChat(userid);
    }



}
