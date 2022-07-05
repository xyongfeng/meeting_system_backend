package com.xyongfeng.util;

import com.xyongfeng.pojo.Users;
import com.xyongfeng.pojo.Param.UsersAddParam;
import com.xyongfeng.pojo.Param.UsersUpdateParam;

/**
 * Users字段转换器
 * @author xyongfeng
 */

public class UserParamConverter {

    public static Users getUsers(UsersUpdateParam usersUpdateParam){
        return new Users()
                .setId(usersUpdateParam.getId())
                .setPassword(usersUpdateParam.getPassword())
                .setName(usersUpdateParam.getName())
                .setEmail(usersUpdateParam.getEmail())
                .setHeadImage(usersUpdateParam.getHeadImage())
                .setIsAdmin(usersUpdateParam.getIsAdmin())
                .setUsername(usersUpdateParam.getUsername())
                .setTelephone(usersUpdateParam.getTelephone());
    }

    public static Users getUsers(UsersAddParam usersAddParam){
        return new Users()
                .setName(usersAddParam.getName())
                .setPassword(usersAddParam.getPassword())
                .setEmail(usersAddParam.getEmail())
                .setHeadImage(usersAddParam.getHeadImage())
                .setIsAdmin(usersAddParam.getIsAdmin())
                .setUsername(usersAddParam.getUsername())
                .setTelephone(usersAddParam.getTelephone());
    }

}
