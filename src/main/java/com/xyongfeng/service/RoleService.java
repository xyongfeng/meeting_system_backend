package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.RoleParam;
import com.xyongfeng.pojo.Role;

import java.util.List;
import java.util.Map;

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
     * 后端系统权限认证调用
     * @return
     */
    List<Role> selectRoleWithUserid(Integer uid);
    /**
     * 查看管理员权限
     * 开放的接口调用
     * 区别:不会输出隐藏权限
     * @param uid
     * @return
     */
    JsonResult selectRoleWithoutHidden(Integer uid);

    /**
     * 修改管理员权限
     * @param uid
     * @param roleParam
     * @return
     */
    JsonResult updateRole(Integer uid, Map<Integer, Boolean> roleParam);

    /**
     * 查看管理员权限列表
     * @return
     */
    JsonResult selectAllRole();
}
