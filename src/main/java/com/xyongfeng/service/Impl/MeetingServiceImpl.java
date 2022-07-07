package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        if (list.size() == 0) {
            return JsonResult.error("查询失败，页码超过已有大小");
        }
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

        assert users != null;
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
}
