package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.UsersFriendInform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-08-22
 */
@Component
public interface UsersFriendInformMapper extends BaseMapper<UsersFriendInform> {

    IPage<UsersFriendInform> selectPageWithFromerInfo(Page<UsersFriendInform> page, @Param("ew") QueryWrapper<UsersFriendInform> orderByDesc);
}
