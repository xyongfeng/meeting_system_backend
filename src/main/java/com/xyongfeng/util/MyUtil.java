package com.xyongfeng.util;


import com.alibaba.fastjson.JSONObject;
import com.xyongfeng.pojo.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


public class MyUtil {

    public static Users getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (Users) authentication.getPrincipal();
    }

}
