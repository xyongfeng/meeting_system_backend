package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.MeetingApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-09-12
 */
@Component
public interface MeetingApplicationMapper extends BaseMapper<MeetingApplication> {

    IPage<MeetingApplication> selectMeetingApplications(Integer uid, Page<MeetingApplication> objectPage);
}
