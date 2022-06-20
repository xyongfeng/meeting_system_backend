package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    public Users userLogin(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        List<Users> list = userMapper.selectByMap(map);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int userAdd(Users users) throws Exception {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", users.getUsername());
        List<Map<String, Object>> list = userMapper.selectMaps(wrapper);
        if (list.size() > 0) {
            throw new Exception("用户名重复");
        }
        return userMapper.insert(users);
    }

    @Override
    public List<Users> listPage(MyPage myPage) {
        Page<Users> page = new Page<>(myPage.getCurrent(), myPage.getSize());
        userMapper.selectPage(page, null);
        return page.getRecords();
    }

    @Override
    public int userUpdateById(Users users) {
        return userMapper.updateById(users);
    }

    @Override
    public Users userDelById(Integer id) {
        Users users = userMapper.selectById(id);
        if (users != null && userMapper.deleteById(id) > 0) {
            return users;
        }
        return null;
    }
}
