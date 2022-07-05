package com.xyongfeng.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyongfeng.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Param.LongIDParam;
import com.xyongfeng.pojo.Param.MeetingAddParam;
import com.xyongfeng.pojo.Param.MeetingUpdateParam;

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

    List<Meeting> listPage(MyPage myPage, QueryWrapper<Meeting> wrapper);

    List<Meeting> listPage(MyPage myPage);

    List<Meeting> listPageByUserid(MyPage myPage, Integer userid);

    int meetingAdd(MeetingAddParam meeting);

    int meetingUpdateById(MeetingUpdateParam meeting);

    Meeting meetingDelById(Long id);

    JsonResult select(MyPage myPage);

    JsonResult select(MyPage myPage, QueryWrapper<Meeting> wrapper);

    JsonResult add(MeetingAddParam meeting);

    JsonResult update(MeetingUpdateParam meeting);

    JsonResult delete(LongIDParam id);

    JsonResult selectByUser(MyPage myPage);

    JsonResult addByUser(MeetingAddParam meeting);

    JsonResult updateByUser(MeetingUpdateParam meeting);

    JsonResult deleteByUser(LongIDParam id);
}
