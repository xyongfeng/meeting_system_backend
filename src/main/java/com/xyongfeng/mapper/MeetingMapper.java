package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.Meeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */
@Component
public interface MeetingMapper extends BaseMapper<Meeting> {

    List<Meeting> selectOneToOne(Page<Meeting> page, @Param("ew") QueryWrapper<Meeting> wrapper);
}
