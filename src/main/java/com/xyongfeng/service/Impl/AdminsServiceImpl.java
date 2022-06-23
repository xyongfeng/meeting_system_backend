package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.service.AdminsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xyongfeng
 */
@Slf4j
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {
    @Resource
    private AdminsMapper adminsMapper;


    @Override
    public Admins adminLogin(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        List<Admins> adminsList = adminsMapper.selectByMap(map);
        if (adminsList.size() > 0) {
            return adminsList.get(0);
        }
        return null;
    }

    @Override
    public List<Admins> listPage(MyPage myPage) {
        Page<Admins> page = new Page<>(myPage.getCurrent(), myPage.getSize());
        adminsMapper.selectPage(page, null);
        return page.getRecords();
    }

    @Override
    public int adminUpdateById(Admins admins) {

        return adminsMapper.updateById(admins);
    }


    @Override
    public int adminAdd(Admins admins) throws Exception {
        QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        wrapper.eq("username", admins.getUsername());
        List<Map<String, Object>> list = adminsMapper.selectMaps(wrapper);
        if (list.size() > 0) {
            throw new Exception("用户名重复");
        }
        return adminsMapper.insert(admins);
    }

    @Override
    public Admins adminDelById(Integer id) {
        Admins admins = adminsMapper.selectById(id);
        if (admins != null && adminsMapper.deleteById(id) > 0) {
            return admins;
        }
        return null;
    }

    @Override
    public Admins getAdminByUserName(String username) {
        return adminsMapper.selectOne(new QueryWrapper<Admins>().eq("username",username));
    }
}
