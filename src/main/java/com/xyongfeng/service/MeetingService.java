package com.xyongfeng.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyongfeng.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Param.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */
public interface MeetingService extends IService<Meeting> {

    JsonResult select(PageParam pageParam);

    JsonResult select(PageParam pageParam, QueryWrapper<Meeting> wrapper);

    JsonResult add(MeetingAddParam meeting);

    JsonResult update(MeetingUpdateParam meeting);

    JsonResult delete(String mid);

    JsonResult selectByUser(PageParam pageParam);

    JsonResult addByUser(MeetingAddParam meeting);

    JsonResult updateByUser(MeetingUpdateParam meeting);

    JsonResult deleteByUser(String mid);

    JsonResult setLicence(MeetSetLicenceParam param);

    JsonResult setLicenceByUser(MeetSetLicenceParam param);

    /**
     * 分页查看自己加入的会议列表
     *
     * @param pageParam
     * @return
     */
    JsonResult selectByUserJoined(PageParam pageParam);


    JsonResult joinMeeting(String mid);

    JsonResult outMeeting(String mid);

    JsonResult getMeetingById(String id, Boolean isAdmin);

    /**
     * 分页查看自己会议的用户签到列表
     *
     * @param mid
     * @param current
     * @param size
     * @return
     */
    JsonResult selectHadSignInList(String mid, Integer current, Integer size);

    JsonResult selectMeetingApplications(Integer current, Integer size);

    JsonResult replyMeetingApplication(String meetingid, Integer userid, Integer result);


    /**
     * 获得自己参加会议的通知列表(未读)
     * @param current
     * @param size
     * @return
     */
    JsonResult selectMeetingNoticePush(Integer current, Integer size);

    /**
     * 对会议通知进行已读
     *
     * @param informId
     * @return
     */
    JsonResult readMeetinInfrom(Integer informId);

    /**
     * 为会议加入公告
     *
     * @param meetingNotice
     * @return
     */
    JsonResult addMeetingNotice(MeetingNotice meetingNotice);

    /**
     * 获取mid对应会议的公告列表
     *
     * @param mid
     * @return
     */
    JsonResult getMeetingNoticeById(String mid, Integer current, Integer size);

    /**
     * 删除指定id的公告
     *
     * @param noticeId
     * @return
     */
    JsonResult delMeetingNoticeById(String meeingId,Integer noticeId);

    /**
     * 编辑会议公告
     * @return
     */
    JsonResult updateMeetingNotice(MeetingNotice meetingNotice);



}
