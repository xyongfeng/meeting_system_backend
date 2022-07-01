package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Menu;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
public interface MenuService extends IService<Menu> {
    /**
     * 通过用户ID查询菜单列表
     * @return
     */

    JsonResult getMenusByUserId();
}
