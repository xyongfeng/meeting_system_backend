package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据用户id获得Role列表
     * @return
     */
    List<Role> selectRoleWithUserid(Integer uid);
}
