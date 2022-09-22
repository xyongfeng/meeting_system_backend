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

    public static JSONObject getClassFields(Object obj) {
        JSONObject jsonObject = new JSONObject();


        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if(f.get(obj) == null){
                    continue;
                }
                jsonObject.put(f.getName(), f.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return jsonObject;

    }
}
