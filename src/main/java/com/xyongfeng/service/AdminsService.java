package com.xyongfeng.service;

import com.xyongfeng.pojo.Admins;
import org.apache.ibatis.annotations.Param;


/**
 * @author xyongfeng
 */

public interface AdminsService {
    /**
     * 登录管理页面
     * @param username 账号
     * @param password 密码
     * @return 登录是否成功
     */
    Admins adminLogin(String username,String password);
    /**
     * 增加新的管理员
     * @param admins 增加的对象
     * @return 增加是否成功
     */
    boolean adminAdd(Admins admins);
}
