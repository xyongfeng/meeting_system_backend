package com.xyongfeng.service;

import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.pojo.Admins;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xyongfeng
 */
@Service
public class AdminsServiceImpl implements AdminsService{
    @Resource
    private AdminsMapper adminsMapper;
    /**
     * 登录管理页面
     * @param username 账号
     * @param password 密码
     * @return 登录是否成功
     */
    @Override
    public Admins adminLogin(String username, String password) {

        return adminsMapper.selectOneByAccount(username);
    }

    /**
     * 增加新的管理员
     *
     * @param admins 增加的对象
     * @return 增加是否成功
     */
    @Override
    public boolean adminAdd(Admins admins) {
        return false;
    }
}
