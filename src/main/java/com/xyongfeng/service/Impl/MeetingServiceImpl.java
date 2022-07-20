package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.mapper.MeetingUsersMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.mapper.MeetingMapper;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.MeetingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.util.MeetingParamConverter;
import com.xyongfeng.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-02
 */
@Service
@Slf4j
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements MeetingService {
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private MeetingUsersMapper meetingUsersMapper;

    @Override
    public int meetingAdd(MeetingAddParam meeting) {
        Meeting meetingx = MeetingParamConverter.getMeeting(meeting);
        // 获取登录人的id
        Users principal = (Users)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        meetingx.setUserId(principal.getId());
        // 获取现在时间
        meetingx.setCreateDate(LocalDateTime.now());
        return meetingMapper.insert(meetingx);
    }

    @Override
    public int meetingUpdateById(MeetingUpdateParam meeting) {

        return meetingMapper.updateById(MeetingParamConverter.getMeeting(meeting));
    }

    @Override
    public Meeting meetingDelById(String id) {
        Meeting meeting = meetingMapper.selectById(id);
        if (meeting != null && meetingMapper.deleteById(id) > 0) {
            return meeting;
        }
        return null;
    }


    @Override
    public List<Meeting> listPageByUserid(PageParam pageParam, Integer userid) {
        QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userid);
        return listPage(pageParam,wrapper);
    }

    @Override
    public List<Meeting> listPage(PageParam pageParam, QueryWrapper<Meeting> wrapper) {
        Page<Meeting> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());

        return meetingMapper.selectOneToOne(page,wrapper);
    }

    @Override
    public List<Meeting> listPage(PageParam pageParam) {
        return listPage(pageParam,null);
    }

    @Override
    public JsonResult select(PageParam pageParam) {

        return select(pageParam,null);
    }

    @Override
    public JsonResult select(PageParam pageParam, QueryWrapper<Meeting> wrapper) {
        List<Meeting> list = listPage(pageParam,wrapper);

        return JsonResult.success(list);
    }


    @Override
    public JsonResult add(MeetingAddParam meeting) {
        try {
            meetingAdd(meeting);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("添加成功", meeting);
    }

    @Override
    public JsonResult update(MeetingUpdateParam meeting) {
        if (meetingUpdateById(meeting) > 0) {
            return JsonResult.success("修改成功", meeting);
        } else {
            return JsonResult.error("修改失败");
        }
    }

    @Override
    public JsonResult delete(LongIDParam id) {
        Meeting delMeeting = meetingDelById(id.getId());
        if (delMeeting != null) {
            return JsonResult.success("删除成功", delMeeting);
        } else {
            return JsonResult.error("删除失败");
        }
    }


    @Override
    public JsonResult selectByUser(PageParam pageParam) {
        Users users = SecurityUtil.getUsers();
        // 只输出当前用户创建的会议
        if(users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }
        return select(pageParam,new QueryWrapper<Meeting>().eq("user_id",users.getId()));
    }

    /**
     * 查看这个会议是否属于这个用户
     * @param meetingId
     * @return
     */
    private boolean isBelongUser(String meetingId){
        QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
        Users users = SecurityUtil.getUsers();
        if (users == null){
            return false;
        }
        wrapper.eq("id",meetingId);
        wrapper.eq("user_id",users.getId());
        Meeting meeting = meetingMapper.selectOne(wrapper);
        return meeting != null;
    }

    @Override
    public JsonResult addByUser(MeetingAddParam meeting) {

        return add(meeting);
    }

    @Override
    public JsonResult updateByUser(MeetingUpdateParam meeting) {
        if(!isBelongUser(meeting.getId())){
            return JsonResult.error("修改失败");
        }
        return update(meeting);
    }

    @Override
    public JsonResult deleteByUser(LongIDParam id) {
        if(!isBelongUser(id.getId())){
            return JsonResult.error("删除失败");
        }
        return delete(id);
    }


    @Override
    public JsonResult setLicence(MeetSetLicenceParam param) {
        int i = meetingMapper.updateById(
                new Meeting()
                        .setId(param.getId())
                        .setHaveLicence(param.getHaveLicence()));
        if (i != 0) {
            return JsonResult.success("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    @Override
    public JsonResult setLicenceByUser(MeetSetLicenceParam param) {
        if(!isBelongUser(param.getId())){
            return JsonResult.error("修改失败");
        }
        return setLicence(param);
    }

    /**
     * 分页查看自己加入的会议列表
     * @param pageParam
     * @return
     */
    @Override
    public JsonResult selectByUserJoined(PageParam pageParam) {
        Users users = SecurityUtil.getUsers();
        // 只输出当前用户创建的会议
        if(users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }

        Page<Meeting> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());

        List<Meeting> meetings = meetingMapper.selectOneToMany(page,users.getId());
        return JsonResult.success(meetings);
    }



    private JsonResult joinMeeting(LongIDParam parm,Boolean isOut) {
        Meeting meeting = meetingMapper.selectOne((new QueryWrapper<Meeting>().eq("id", parm.getId())));
        if(meeting == null){
            return JsonResult.error("会议没找到，请检查会议号");
        }
        Users users = SecurityUtil.getUsers();
        if(users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }
        QueryWrapper<MeetingUsers> wrapper = (new QueryWrapper<MeetingUsers>())
                .eq("users_id", users.getId())
                .eq("meeting_id", meeting.getId());
        if(isOut){

            int state = meetingUsersMapper.delete(wrapper);

            return state > 0 ? JsonResult.success("退出成功") : JsonResult.error("退出失败");
        }
        if(meetingUsersMapper.selectOne(wrapper) != null){
            return JsonResult.error("你已经参加此会议了");
        }

        // todo 要写申请入会
        meetingUsersMapper.insert((new MeetingUsers())
                .setMeetingId(meeting.getId())
                .setUsersId(users.getId()));
        return JsonResult.success("参加成功");
    }

    @Override
    public JsonResult joinMeeting(LongIDParam parm) {
        return joinMeeting(parm,false);
    }

    @Override
    public JsonResult outMeeting(LongIDParam parm) {
        return joinMeeting(parm,true);
    }
}
