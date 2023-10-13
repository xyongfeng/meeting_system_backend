package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.util.JwtTokenUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;


/**
 * @author xyongfeng
 */

public interface UsersService extends IService<Users> {

    /**
     * 登录
     *
     * @param users
     * @param request
     * @param jwtTokenUtil
     * @param userDetailsService
     * @param passwordEncoder
     * @return
     */
    JsonResult login(UsersLoginParam users, HttpServletRequest request, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder);

    JsonResult register(UsersRegisterParam users, HttpServletRequest request, PasswordEncoder passwordEncoder);

    /**
     * 获取当前登录的用户信息
     *
     * @param principal
     * @return
     */
    JsonResult getAdminInfo(Principal principal, HttpServletRequest servletRequest);

    JsonResult select(Integer current, Integer size);

    JsonResult add(UsersAddParam users, PasswordEncoder passwordEncoder);

    JsonResult update(UsersUpdateParam users, Integer uid, PasswordEncoder passwordEncoder);

    JsonResult delete(Integer uid);

    /**
     * 设置管理员
     *
     * @param param
     * @return
     */
    JsonResult setAdmin(UsersSetAdminParam param);

    /**
     * 设置头像
     *
     * @param param
     * @return
     */
    JsonResult setHeadImg(UsersSetImgParam param);

    /**
     * 设置面部照片
     *
     * @param param
     * @return
     */
    JsonResult setFaceImg(UsersSetImgParam param);


    /**
     * 会议签到
     *
     * @param param
     * @param meetingId
     * @return
     */
    JsonResult signIn(ImgBase64Param param, String meetingId);

    /**
     * 是否已经签到
     *
     * @param meetingId
     * @return
     */
    JsonResult hadSignIn(String meetingId);

    /**
     * 通过姓名获取该用户的信息
     *
     * @param userName
     * @return
     */
    JsonResult getUserInfoByName(String userName);

    /**
     * 发送好友申请
     *
     * @param userid
     * @return
     */
    JsonResult sendFriApplication(Integer userid);

    /**
     * 回应好友申请
     *
     * @param userid
     * @param result 0 忽略 1 同意
     * @return
     */
    JsonResult replyFriApplication(Integer userid, Integer result);

    /**
     * 查看好友申请列表(未读)
     *
     * @return
     */
    JsonResult getFriApplications(Integer current, Integer size);

    /**
     * 根据userid删除好友
     *
     * @param userid
     * @return
     */
    JsonResult delFriendById(Integer userid);

    /**
     * 查看好友列表
     *
     * @return
     */
    JsonResult getFriendsAndChat(Integer current, Integer size);

    /**
     * 查看与好友聊天记录
     *
     * @param userid
     * @return
     */
    JsonResult getFriChat(Integer userid, Integer current, Integer size);

    /**
     * 向好友发送消息
     *
     * @param userid
     * @param conetent
     * @return
     */
    JsonResult sendFriChat(Integer userid, String conetent);

    /**
     * 通过uid获取该用户的信息
     *
     * @param uid
     * @return
     */
    JsonResult getUserInfoById(Integer uid);

    JsonResult readFriChat(Integer userid);


    JsonResult updateOwnerInfo(UsersUpdateWithOwnerParam users);

    JsonResult updateOwnerPass(UsersUpdatePassParam users, PasswordEncoder passwordEncoder, HttpServletRequest request);

    /**
     * 接收base64,设置用户面部照片
     *
     * @param param
     * @return
     */
    JsonResult setFaceImgWithBase64(ImgBase64Param param);

    /**
     * 接收base64,人脸识别登录
     *
     * @param param
     * @return
     */
    JsonResult loginWithFace(ImgBase64Param param, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService);

    /**
     * 接收base64,人脸识别验证
     *
     * @param param
     * @return
     */
    JsonResult faceVerification(ImgBase64Param param);


    /**
     * 根据用户名获取用户信息，用于jwt验证
     * @param username
     * @return
     */
    UserDetails getUserByUserName(String username);

    /**
     * 退出登录
     * @return
     */
    JsonResult logout();
}
