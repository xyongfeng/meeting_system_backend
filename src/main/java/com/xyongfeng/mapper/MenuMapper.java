package com.xyongfeng.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.Menu;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Component
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 通过用户ID查询菜单列表
     * @param id
     * @return
     */

    List<Menu> getMenusByUserId(Integer id);
}
