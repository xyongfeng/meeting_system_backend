package com.xyongfeng.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.MeetingNoticeUsers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-09-12
 */
@Component
public interface MeetingNoticeUsersMapper extends BaseMapper<MeetingNoticeUsers> {
    /**
     * 将之前的推送公告加入到该用户通知中
     * @param mid
     * @param uid
     */
    void insertMeetingNoticePushToUser(@Param("mid") String mid, @Param("uid") Integer uid);

}
