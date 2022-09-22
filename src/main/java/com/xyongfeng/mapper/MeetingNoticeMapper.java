package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyongfeng.pojo.MeetingNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface MeetingNoticeMapper extends BaseMapper<MeetingNotice> {
    /**
     * 输出该用户未读的会议通知
     * @param uid
     * @param page
     * @return
     */
    IPage<MeetingNotice> selectMeetingNoticePushWithUid(@Param("uid") Integer uid, IPage<MeetingNotice> page);

    /**
     * 输出该会议的公告
     * @param mid
     * @param page
     * @return
     */
    IPage<MeetingNotice> selectMeetingNotice(@Param("mid") String mid, IPage<MeetingNotice> page);

}
