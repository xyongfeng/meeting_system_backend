package com.xyongfeng.mapper;

import com.xyongfeng.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
