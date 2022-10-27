package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.Role;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Component
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id获得Role列表
     *
     * @return
     */
    List<Role> selectRoleWithUserid(Integer uid);

    /**
     * 不会输出隐藏role
     * @param uid
     * @return
     */
    List<Role> selectRoleWithoutHidden(Integer uid);


}
