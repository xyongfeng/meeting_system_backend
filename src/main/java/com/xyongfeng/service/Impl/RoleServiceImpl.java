package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.RoleMapper;
import com.xyongfeng.mapper.UsersRoleMapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.RoleParam;
import com.xyongfeng.pojo.Role;
import com.xyongfeng.pojo.UsersRole;
import com.xyongfeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UsersRoleMapper usersRoleMapper;

    /**
     * 根据用户id获得Role列表
     *
     * @return
     */
    @Override
    public List<Role> selectRoleWithUserid(Integer uid) {
        return roleMapper.selectRoleWithUserid(uid);
    }

    @Override
    public JsonResult selectRoleWithoutHidden(Integer uid) {

        return JsonResult.success(roleMapper.selectRoleWithoutHidden(uid));
    }

    @Override
    public JsonResult selectAllRole() {
        return JsonResult.success(roleMapper.selectList(new QueryWrapper<Role>().eq("hidden_xq", 0)));
    }

    @Override
    public JsonResult updateRole(Integer uid, Map<Integer, Boolean> roleParam) {

        roleParam.forEach((k, v) -> {
//            System.out.println(k == 1);
//            System.out.println(v.equals(true));
            usersRoleMapper.delete(new QueryWrapper<UsersRole>()
                    .eq("user_id_xq", uid)
                    .eq("role_id_xq", k));
            if (v) {
                UsersRole usersRole = new UsersRole().setUserId(uid).setRoleId(k);
                usersRoleMapper.insert(usersRole);

            }
        });


        return JsonResult.success("权限修改成功");
    }
}
