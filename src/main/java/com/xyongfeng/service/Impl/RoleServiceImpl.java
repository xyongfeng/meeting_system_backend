package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.RoleMapper;
import com.xyongfeng.pojo.Role;
import com.xyongfeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据用户id获得Role列表
     * @return
     */
    @Override
    public List<Role> selectRoleWithUserid(Integer uid) {
        return roleMapper.selectRoleWithUserid(uid);
    }
}
