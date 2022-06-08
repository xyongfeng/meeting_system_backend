package com.xyongfeng.mapper;


import com.xyongfeng.pojo.Admins;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xyongfeng
 */
@Mapper
public interface AdminsMapper {
    /**
     * 返回所有admins对象
     * @return admins对象的列表
     */
    @Select("select * from admins")
    List<Admins> selectAll();

    /**
     * 返回通过account查找的对象
     * @param username 账号
     * @return Admins对象
     */
    @Select("select * from admins where username = #{username}")
    Admins selectOneByAccount(String username);




}
