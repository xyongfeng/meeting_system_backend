package com.xyongfeng.mapper;

import com.xyongfeng.pojo.UsersFaceFeature;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-11-28
 */
@Component
public interface UsersFaceFeatureMapper extends BaseMapper<UsersFaceFeature> {

    List<UsersFaceFeature> selectAll();
}
