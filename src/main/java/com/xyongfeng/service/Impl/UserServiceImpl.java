package com.xyongfeng.service.Impl;

import com.xyongfeng.pojo.User;
import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User insert(User user) {
        return null;
    }

    @Override
    public boolean deleteById() {
        return false;
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectList(null);
    }


}
