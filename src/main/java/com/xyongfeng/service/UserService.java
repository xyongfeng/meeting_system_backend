package com.xyongfeng.service;

import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-09
 */
public interface UserService extends IService<Users> {
    /**
     * 用户登录
     * @param username 账号
     * @param password 密码
     * @return 登录是否成功
     */
    Users userLogin(String username, String password);
    /**
     * 增加新的用户
     * @param users 增加的对象
     * @return 返回执行状态
     *
     */
    int userAdd(Users users) throws Exception;
    /**
     * 分页获取用户列表
     * @param myPage current(当前页码),size(页码大小)
     *
     * @return 用户列表
     */
    List<Users> listPage(MyPage myPage);
    /**
     * 根据id修改用户
     * @param admins 修改的对象
     *
     * @return 修改结果
     */
    int userUpdateById(Users users);
    /**
     * 根据id删除用户
     * @param id 删除的用户id
     *
     * @return 删除结果
     */
    Users userDelById(Integer id);

    /**
     * 通过username获取admin
     * @param username
     * @return
     */
    Users getUserByUserName(String username);
}
