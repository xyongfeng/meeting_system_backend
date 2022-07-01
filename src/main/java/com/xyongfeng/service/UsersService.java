package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.pojo.UsersAddParam;
import com.xyongfeng.pojo.UsersUpdateParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     * @param myPage current(当前页码),size(页码大小)
     * @return 管理员列表
     */
    List<Users> listPage(MyPage myPage);

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
}
