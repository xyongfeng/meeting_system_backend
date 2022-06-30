package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-25
 */
@Component
public interface MeetingMapper extends BaseMapper<Meeting> {

}
