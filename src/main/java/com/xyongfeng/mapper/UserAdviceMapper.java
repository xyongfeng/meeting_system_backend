package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.AdminLog;
import com.xyongfeng.pojo.UserAdvice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Component
public interface UserAdviceMapper extends BaseMapper<UserAdvice> {

    IPage<UserAdvice> selectWithUserAndImg(Page<UserAdvice> page);

    UserAdvice selectWithUserAndImgById(@Param("userId") Integer userId);
}
