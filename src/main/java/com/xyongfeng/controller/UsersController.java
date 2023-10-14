package com.xyongfeng.controller;

import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.JwtTokenUtil;
import com.xyongfeng.util.MyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Api(tags = "普通用户操作user接口及基本操作")
@RestController
@Slf4j
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @ApiOperation("登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody @Validated UsersLoginParam users, HttpServletRequest request
    ) {
        log.info(String.format("post:/users/login，进行登录,%s", users));
        return usersService.login(users, request, jwtTokenUtil, userDetailsService, passwordEncoder);
    }

    @ApiOperation("接收base64,人脸识别登录")
    @PostMapping("/loginWithFace")
    public JsonResult loginWithFace(@RequestBody @Validated ImgBase64Param param) {
        log.info("post:/loginWithFace 接收base64,人脸识别登录。");
        return usersService.loginWithFace(param, jwtTokenUtil, userDetailsService);
    }

    @ApiOperation("接收base64,人脸识别验证")
    @PostMapping("/faceVerification")
    public JsonResult faceVerification(@RequestBody @Validated ImgBase64Param param) {
        log.info("post:/faceVerification 接收base64,人脸识别验证。");
        return usersService.faceVerification(param);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public JsonResult register(@RequestBody @Validated UsersRegisterParam users, HttpServletRequest request
    ) {
        log.info(String.format("post:/users/register，进行注册,%s", users));
        return usersService.register(users, request, passwordEncoder);
    }

    @ApiOperation(value = "获取当前登录的用户信息")
    @GetMapping("/info")
    public JsonResult getAdminInfo(Principal principal, HttpServletRequest servletRequest) {

        return usersService.getAdminInfo(principal, servletRequest);
    }

    @ApiOperation(value = "修改本人基本信息")
    @PutMapping("/info")
    public JsonResult updateOwnerInfo(@RequestBody @Validated UsersUpdateWithOwnerParam users) {
        return usersService.updateOwnerInfo(users);
    }

    @ApiOperation(value = "修改本人密码")
    @PutMapping("/info/password")
    public JsonResult updateOwnerPass(@RequestBody @Validated UsersUpdatePassParam users, HttpServletRequest request) {
        return usersService.updateOwnerPass(users, passwordEncoder, request);
    }

    @ApiOperation("设置用户头像")
    @PutMapping("/info/headImg")
    public JsonResult setHeadImg(@Validated UsersSetImgParam param) {
        param.setId(MyUtil.getUsers().getId());
        log.info(String.format("Put:/users/info/headImg 设置用户头像。%s", param));
        return usersService.setHeadImg(param);
    }

    @ApiOperation("设置用户面部照片")
    @PutMapping("/info/faceImg")
    public JsonResult setFaceImg(@Validated UsersSetImgParam param) {
        log.info(String.format("Put:/users/info/faceImg 设置用户面部照片。%s", param));
        return usersService.setFaceImg(param);
    }

    @ApiOperation("接收base64,设置用户面部照片")
    @PostMapping("/info/faceImg")
    public JsonResult setFaceImgWithBase64(@RequestBody @Validated ImgBase64Param param) {
        log.info(String.format("Post:/users/info/faceImg 设置用户面部照片。%s", param));
        return usersService.setFaceImgWithBase64(param);
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

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public JsonResult logout() {


        return usersService.logout();
    }

    @ApiOperation(value = "签到")
    @PostMapping("/signIn/{mid}")
    public JsonResult signIn(@RequestBody @Validated ImgBase64Param param, @PathVariable String mid) {
        return usersService.signIn(param, mid);
    }


    @ApiOperation(value = "检查是否签到")
    @GetMapping("/signIn/{mid}")
    public JsonResult getSignIn(@PathVariable String mid) {
        return usersService.hadSignIn(mid);
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
