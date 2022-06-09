package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.User;
import com.xyongfeng.service.AdminsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xyongfeng
 */
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {
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
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        List<Admins> adminsList = adminsMapper.selectByMap(map);
        if(adminsList.size() > 0) {
            return adminsList.get(0);
        }
        return null;
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
