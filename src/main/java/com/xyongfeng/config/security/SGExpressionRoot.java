package com.xyongfeng.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 自定义验证规则
 */
@Component
public class SGExpressionRoot {
    public boolean hasAuthority(String auth){
        // 获取当前登录用户权限
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // 查看是否有auth权限
        for(GrantedAuthority authority:authorities){
            if(authority.getAuthority().equals(auth)){
                return true;
            }
        }
        return false;

    }
}
