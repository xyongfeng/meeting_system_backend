package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Meeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.Users;
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
    /**
     * 会议与创建的用户一一对应
     * @param page
     * @param wrapper
     * @return
     */
    List<Meeting> selectOneToOne(Page<Meeting> page, @Param("ew") QueryWrapper<Meeting> wrapper);

    /**
     * 一个用户与多个会议对应
     * @param page
     * @param userId
     * @return
     */
    List<Meeting> selectOneToMany(Page<Meeting> page, @Param("user_id") Integer userId);
}
