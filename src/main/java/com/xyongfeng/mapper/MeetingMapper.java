package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Meeting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.MeetingUsers;
import com.xyongfeng.pojo.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */
@Component
public interface MeetingMapper extends BaseMapper<Meeting> {


    @Override
    Meeting selectById(@Param("mid") Serializable id);

    /**
     * 会议与创建的用户一一对应 输出列表
     *
     * @param page
     * @param wrapper
     * @return
     */
    IPage<Meeting> selectMeetingWithCreater(Page<Meeting> page, @Param("ew") QueryWrapper<Meeting> wrapper);

    /**
     * 输出id对应的会议携带创建者users 输出单个
     *
     * @param meetId
     * @return
     */
    Meeting selectMeetingWithCreaterOne(@Param("meeting_id") String meetId);

    /**
     * 输出当前用户参与的会议列表
     *
     * @param page
     * @param userId
     * @return
     */
//    IPage<Meeting> selectMeetingByParticipants(Page<Meeting> page, @Param("user_id") Integer userId);

    /**
     * 输出当前用户参与的会议列表有条件
     *
     * @param page
     * @param userId
     * @return
     */
    IPage<Meeting> selectMeetingByParticipants(Page<Meeting> page, @Param("user_id") Integer userId, @Param("ew") QueryWrapper<MeetingUsers> wrapper);

    /**
     * 获得用户参与表中是否存在此会议
     *
     * @param meetId
     * @param userId
     */
    Meeting getExistMeetWithUser(@Param("meeting_id") String meetId, @Param("user_id") Integer userId);

    /**
     * 输出该会议的参与者列表
     *
     * @param meetId
     * @return
     */
    List<Users> selectOneWithParticipantById(@Param("meeting_id") String meetId);

    /**
     * 输出用户参与的已结束的会议，也就是历史会议
     */
    IPage<Meeting> selectMeetingByParticipantsWithEnd(Page<Meeting> objectPage, @Param("user_id") Integer id);

    /**
     * 为结束的会议，写上结束时间
     *
     * @param mid
     * @param now
     * @return
     */
    Integer updateEndDate(@Param("mid") String mid, @Param("endDate") String now);

    /**
     * 根据id删除自己的历史会议
     *
     * @param mid
     * @param uid
     * @return
     */
    Integer delHistoryMeeting(@Param("mid") String mid, @Param("uid") Integer uid);

    /**
     * 获取所有会议开始时间
     *
     * @return
     */
    List<LocalDateTime> selectAllStartDateTime(@Param("withoutEnd") Boolean withoutEnd);
}
