package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyongfeng.pojo.MeetingPassword;
import com.xyongfeng.mapper.MeetingPasswordMapper;
import com.xyongfeng.service.MeetingPasswordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2023-01-03
 */
@Service
public class MeetingPasswordServiceImpl extends ServiceImpl<MeetingPasswordMapper, MeetingPassword> implements MeetingPasswordService {
    @Autowired
    private MeetingPasswordMapper meetingPasswordMapper;

    @Override
    public int insert(String mid, String password, Boolean passEnabled) {
        return meetingPasswordMapper.insert(new MeetingPassword().setMeetingId(mid).setPassword(password).setEnabled(passEnabled));
    }

    @Override
    public MeetingPassword getMeetingPasswordOne(String mid) {

        return meetingPasswordMapper.selectOne(new QueryWrapper<MeetingPassword>().eq("meeting_id_xq", mid));
    }
}
