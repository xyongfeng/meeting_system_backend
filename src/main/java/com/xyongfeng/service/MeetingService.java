package com.xyongfeng.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyongfeng.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Param.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */
public interface MeetingService extends IService<Meeting> {

    List<Meeting> listPage(PageParam pageParam, QueryWrapper<Meeting> wrapper);

    List<Meeting> listPage(PageParam pageParam);

    List<Meeting> listPageByUserid(PageParam pageParam, Integer userid);

    int meetingAdd(MeetingAddParam meeting);

    int meetingUpdateById(MeetingUpdateParam meeting);

    Meeting meetingDelById(String id);

    JsonResult select(PageParam pageParam);

    JsonResult select(PageParam pageParam, QueryWrapper<Meeting> wrapper);

    JsonResult add(MeetingAddParam meeting);

    JsonResult update(MeetingUpdateParam meeting);

    JsonResult delete(LongIDParam id);

    JsonResult selectByUser(PageParam pageParam);

    JsonResult addByUser(MeetingAddParam meeting);

    JsonResult updateByUser(MeetingUpdateParam meeting);

    JsonResult deleteByUser(LongIDParam parm);

    JsonResult setLicence(MeetSetLicenceParam param);

    JsonResult setLicenceByUser(MeetSetLicenceParam param);

    /**
     * 分页查看自己加入的会议列表
     * @param pageParam
     * @return
     */
    JsonResult selectByUserJoined(PageParam pageParam);

    JsonResult joinMeeting(LongIDParam parm);

    JsonResult outMeeting(LongIDParam parm);

    JsonResult getMeetingById(Long id,Boolean isAdmin);
}
