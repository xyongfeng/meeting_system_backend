package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.MenuMapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.xyongfeng.pojo.Menu;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;


    @Override
    public JsonResult getMenusByUserId() {
        List<Menu> menus = menuMapper.getMenusByUserId(((Users) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getId());
        return JsonResult.success(menus);
    }


}
