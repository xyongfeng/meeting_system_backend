package com.xyongfeng.util;


import com.xyongfeng.pojo.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


public class SecurityUtil {

    public static Users getUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication == null) {
            return null;
        }
        return (Users) authentication.getPrincipal();
    }
}
