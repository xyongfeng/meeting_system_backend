package com.xyongfeng.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.mapper.MeetingUsersMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


/**
 * @author xyongfeng
 */

public interface UsersService extends IService<Users> {

    /**
     * 登录管理页面
     *
     * @param username 账号
     * @param password 密码
     * @return 登录是否成功
     */
    Users adminLogin(String username, String password);

    /**
     * 增加新的管理员
     *
     * @param users 增加的对象
     * @return 返回执行状态
     */
    int userAdd(UsersAddParam users, PasswordEncoder passwordEncoder) throws Exception;

    /**
     * 分页获取管理员列表
     *
     * @param pageParam current(当前页码),size(页码大小)
     * @return 管理员列表
     */
    IPage<Users> listPage(PageParam pageParam);

    /**
     * 根据id修改管理员
     *
     * @param users users
     * @return 修改结果
     */
    int userUpdateById(UsersUpdateParam users);

    /**
     * 根据id删除管理员
     *
     * @param id 删除的管理员id
     * @return 删除结果
     */
    Users userDelById(Integer id);

    /**
     * 通过username获取admin
     *
     * @param username
     * @return
     */
    Users getUserByUserName(String username);

    /**
     * 设置管理员
     *
     * @param param
     * @return
     */
    JsonResult setAdmin(UsersSetAdminParam param);

    /**
     * 设置头像
     * @param param
     * @return
     */
    JsonResult setHeadImg(UsersSetImgParam param);

    /**
     * 设置面部照片
     * @param param
     * @return
     */
    JsonResult setFaceImg(UsersSetImgParam param);

    /**
     * 会议签到
     * @param param
     * @param meetingId
     * @return
     */
    JsonResult signIn(UsersSignInParam param,String meetingId);

    /**
     * 是否已经签到
     * @param meetingId
     * @return
     */
    JsonResult hadSignIn(String meetingId);

    /**
     * 通过姓名获取该用户的信息
     * @param userName
     * @return
     */
    JsonResult getUserInfoByName(String userName);

    /**
     * 发送好友申请
     * @param userid
     * @return
     */
    JsonResult sendFriApplication(Integer userid);

    /**
     * 回应好友申请
     * @param userid
     * @param result 0 忽略 1 同意
     * @return
     */
    JsonResult replyFriApplication(Integer userid, Integer result);

    /**
     * 查看好友申请列表(未读)
     * @return
     */
    JsonResult getFriApplications(Integer current,Integer size);

    /**
     * 根据userid删除好友
     * @param userid
     * @return
     */
    JsonResult delFriendById(Integer userid);

    /**
     * 查看好友列表
     * @return
     */
    JsonResult getFriendsAndChat(Integer current,Integer size);

    /**
     * 查看与好友聊天记录
     * @param userid
     * @return
     */
    JsonResult getFriChat(Integer userid, Integer current, Integer size);

    /**
     * 向好友发送消息
     * @param userid
     * @param conetent
     * @return
     */
    JsonResult sendFriChat(Integer userid, String conetent);

    /**
     * 通过uid获取该用户的信息
     * @param uid
     * @return
     */
    JsonResult getUserInfoById(Integer uid);

    JsonResult readFriChat(Integer userid);
}
