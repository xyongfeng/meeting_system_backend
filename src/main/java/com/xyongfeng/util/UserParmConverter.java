package com.xyongfeng.util;

import com.xyongfeng.pojo.Users;
import com.xyongfeng.pojo.UsersAddParam;
import com.xyongfeng.pojo.UsersUpdateParam;

/**
 * Users字段转换器
 * @author xyongfeng
 */

public class UserParmConverter {

    public static Users getUsers(UsersUpdateParam usersUpdateParam){
        return new Users()
                .setId(usersUpdateParam.getId())
                .setPassword(usersUpdateParam.getPassword())
                .setName(usersUpdateParam.getName())
                .setEmail(usersUpdateParam.getEmail())
                .setFaceImage(usersUpdateParam.getFaceImage())
                .setAdmin(usersUpdateParam.isAdmin())
                .setUsername(usersUpdateParam.getUsername())
                .setTelephone(usersUpdateParam.getTelephone());
    }

    public static Users getUsers(UsersAddParam usersAddParam){
        return new Users()
                .setName(usersAddParam.getName())
                .setPassword(usersAddParam.getPassword())
                .setEmail(usersAddParam.getEmail())
                .setFaceImage(usersAddParam.getFaceImage())
                .setAdmin(usersAddParam.isAdmin())
                .setUsername(usersAddParam.getUsername())
                .setTelephone(usersAddParam.getTelephone());
    }

}
