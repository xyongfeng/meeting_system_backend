package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.UserParmConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xyongfeng
 */
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UserMapper, Users> implements UsersService {
    @Autowired
    private UserMapper usersMapper;

    @Autowired
    private RoleService roleService;


    @Override
    public Users adminLogin(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        List<Users> adminsList = usersMapper.selectByMap(map);
        if (adminsList.size() > 0) {
            return adminsList.get(0);
        }
        return null;
    }

    @Override
    public List<Users> listPage(MyPage myPage) {
        Page<Users> page = new Page<>(myPage.getCurrent(), myPage.getSize());
        usersMapper.selectPage(page, null);
        return page.getRecords();
    }


    @Override
    public int userUpdateById(UsersUpdateParam usersUpdateParam) {

        return usersMapper.updateById(UserParmConverter.getUsers(usersUpdateParam));
    }


    @Override
    public int userAdd(UsersAddParam users,PasswordEncoder passwordEncoder) throws Exception {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", users.getUsername());
        List<Map<String, Object>> list = usersMapper.selectMaps(wrapper);
        if (list.size() > 0) {
            throw new Exception("用户名重复");
        }
        // 对密码加密
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        return usersMapper.insert(UserParmConverter.getUsers(users));
    }

    @Override
    public Users userDelById(Integer id) {
        Users users = usersMapper.selectById(id);
        if (users != null && usersMapper.deleteById(id) > 0) {
            return users;
        }
        return null;
    }

    @Override
    public Users getUserByUserName(String username) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
        List<Role> roles = roleService.selectRoleWithUserid(users.getId());
        List<String> perms = roles.stream().map(Role::getPerms).collect(Collectors.toList());
        if(users.isAdmin()){
            perms.add("admin");
        }
        users.setPerms(perms);
        return users;
    }
}
