package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.socketer.SockerSender;
import com.xyongfeng.mapper.*;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.service.MeetingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.util.MeetingParamConverter;
import com.xyongfeng.util.MyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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
    private UsersMapper usersMapper;
    @Autowired
    private MeetingUsersMapper meetingUsersMapper;
    @Autowired
    private SockerSender sockerSender;
    @Autowired
    private MeetingApplicationMapper meetingApplicationMapper;
    @Autowired
    private MeetingNoticeMapper meetingNoticeMapper;
    @Autowired
    private MeetingNoticeUsersMapper meetingNoticeUsersMapper;


    private Meeting meetingAdd(MeetingAddParam meeting) {
        Meeting meetingx = MeetingParamConverter.getMeeting(meeting);
        // 获取登录人的id
        Users principal = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        meetingx.setUserId(principal.getId());
        // 获取现在时间
        meetingx.setCreateDate(LocalDateTime.now());
        meetingMapper.insert(meetingx);
        return meetingx;
    }

    private int meetingUpdateById(MeetingUpdateParam meeting) {

        return meetingMapper.updateById(MeetingParamConverter.getMeeting(meeting));
    }

    /**
     * 删除会议
     *
     * @param id
     * @return
     */
    private Meeting meetingDelById(String id) {
        Meeting meeting = meetingMapper.selectById(id);
        if (meeting != null && meetingMapper.deleteById(id) > 0) {

            sendHiddenMeetNoticeToAllUser(meeting.getId(), String.format("%s 会议已经结束", meeting.getName()), String.format("%s 会议已经结束", meeting.getName()));

            // 向房间其他人发送刷新行为
            sockerSender.sendActionToMeetUser("refresh", id, meeting.getUserId(), null, true);

            // 写入结束时间
            meetingMapper.updateEndDate(id, LocalDateTime.now().toString());

            return meeting;
        }
        return null;
    }


    private IPage<Meeting> listPageByUserid(PageParam pageParam, Integer userid) {
        QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userid);
        return listPage(pageParam, wrapper);
    }

    private IPage<Meeting> listPage(PageParam pageParam, QueryWrapper<Meeting> wrapper) {
        Page<Meeting> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        return meetingMapper.selectMeetingWithCreater(page, wrapper);
    }

    private IPage<Meeting> listPage(PageParam pageParam) {
        return listPage(pageParam, null);
    }

    @Override
    public JsonResult select(PageParam pageParam) {

        return select(pageParam, null);
    }

    @Override
    public JsonResult select(PageParam pageParam, QueryWrapper<Meeting> wrapper) {
        IPage<Meeting> list = listPage(pageParam, wrapper);

        return JsonResult.success(list);
    }


    @Override
    public JsonResult add(MeetingAddParam meeting) {
        try {
            Meeting meetingx = meetingAdd(meeting);
            addMeetingNotice(
                    new MeetingNotice()
                            .setMeetingId(meetingx.getId())
                            .setTitle(String.format("欢迎来参加 %s 会议", meeting.getName()))
                            .setContent(String.format("我们会议将于 %s 开始", meeting.getStartDate().toString()
                                    .replace('T', ' ')))
                            .setType(2)
            );
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
    public JsonResult delete(String mid) {
        Meeting delMeeting = meetingDelById(mid);
        if (delMeeting != null) {
            return JsonResult.success("结束成功", delMeeting);
        } else {
            return JsonResult.error("结束失败");
        }
    }


    @Override
    public JsonResult selectByUser(PageParam pageParam) {
        Users users = MyUtil.getUsers();
        // 只输出当前用户创建的会议
        if (users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }
        return select(pageParam, new QueryWrapper<Meeting>().eq("user_id", users.getId()));
    }

    /**
     * 查看这个会议是否属于这个用户
     *
     * @param meetingId
     * @return
     */
    private boolean isBelongUser(String meetingId) {
        QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
        Users users = MyUtil.getUsers();
        if (users == null) {
            return false;
        }
        wrapper.eq("id", meetingId);
        wrapper.eq("user_id", users.getId());
        Meeting meeting = meetingMapper.selectOne(wrapper);
        return meeting != null;
    }

    @Override
    public JsonResult addByUser(MeetingAddParam meeting) {

        return add(meeting);
    }

    @Override
    public JsonResult updateByUser(MeetingUpdateParam meeting) {
        if (!isBelongUser(meeting.getId())) {
            return JsonResult.error("修改失败");
        }
        return update(meeting);
    }

    @Override
    public JsonResult deleteByUser(String mid) {
        if (!isBelongUser(mid)) {
            return JsonResult.error("结束失败");
        }
        return delete(mid);
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
        if (!isBelongUser(param.getId())) {
            return JsonResult.error("修改失败");
        }
        return setLicence(param);
    }

    /**
     * 分页查看自己的历史会议列表
     *
     * @return
     */
    @Override
    public JsonResult selectByHistory() {

        Users users = MyUtil.getUsers();
        IPage<Meeting> myMeetings = meetingMapper.selectMeetingWithCreater(new Page<>(0, -1),
                new QueryWrapper<Meeting>()
                        .eq("user_id", users.getId())
                        .eq("to_owner_hidden", 0)
                        .eq("end", 1)
        );
        IPage<Meeting> joinedMeetings = meetingMapper.selectMeetingByParticipantsWithEnd(new Page<>(0, -1), users.getId());

        List<Meeting> records = myMeetings.getRecords();
        records.addAll(joinedMeetings.getRecords());
        // 降序
        records.sort(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o2.getEndDate().compareTo(o1.getEndDate());
            }
        });


        myMeetings.setRecords(records);

        return JsonResult.success(myMeetings);
    }

    /**
     * 分页查看自己加入的会议列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public JsonResult selectByUserJoined(PageParam pageParam) {
        Users users = MyUtil.getUsers();
        // 只输出当前用户创建的会议
        if (users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }

        Page<Meeting> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());

        IPage<Meeting> meetings = meetingMapper.selectMeetingByParticipants(page, users.getId(), null);
        return JsonResult.success(meetings);
    }


    /**
     * isOut为假就是加入会议，isOut为真就是退出会议
     *
     * @param mid
     * @param isOut
     * @return
     */
    private JsonResult joinMeeting(String mid, Boolean isOut) {

        Meeting meeting = meetingMapper.selectById(mid);
        if (meeting == null) {
            return JsonResult.error("会议没找到，请检查会议号");
        }
        if (meeting.getEnd()) {
            return JsonResult.error("会议已经结束");
        }
        Users users = MyUtil.getUsers();
        if (users == null) {
            return JsonResult.error("获取用户信息失败，请重新登录");
        }

        QueryWrapper<MeetingUsers> wrapper = (new QueryWrapper<MeetingUsers>())
                .eq("users_id", users.getId())
                .eq("meeting_id", meeting.getId());

        // 判断是否退出
        if (isOut) {
            int state = meetingUsersMapper.delete(wrapper);
            return state > 0 ? JsonResult.success("退出成功") : JsonResult.error("退出失败");
        }

        if (meeting.getUserId().equals(users.getId()) || meetingUsersMapper.selectOne(wrapper) != null) {
            return JsonResult.error("你已经参加此会议了");
        }

        // 判断是否要申请入会
        if (meeting.getHaveLicence()) {
            MeetingApplication one = meetingApplicationMapper.selectOne(new QueryWrapper<MeetingApplication>()
                    .eq("applicant_id", users.getId()).eq("meeting_id", mid).eq("state", 0));
            if (one != null) {

                return JsonResult.error("申请失败，你已经申请过该会议了");
            }

            MeetingApplication meetingApplication = (new MeetingApplication())
                    .setApplicantId(users.getId())
                    .setMeetingId(meeting.getId())
                    .setState(0)
                    .setSendTime(LocalDateTime.now());

            meetingApplicationMapper.insert(meetingApplication);

            meetingApplication.setMeeting(meeting);
            meetingApplication.setUsers(users);
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(meetingApplication);
            sockerSender.sendInform(jsonObject, 2, meeting.getUserId());
            return JsonResult.success("成功申请加入会议，请等待房主同意");
        }
        joinMeeting(meeting.getId(), users.getId());
        insertMeetingNoticePushToUser(meeting.getId(), users.getId());
        return JsonResult.success("参加成功");
    }

    private void joinMeeting(String mid, Integer uid) {
        meetingUsersMapper.insert((new MeetingUsers())
                .setMeetingId(mid)
                .setUsersId(uid));
    }

    @Override
    public JsonResult joinMeeting(String mid) {
        return joinMeeting(mid, false);
    }


    @Override
    public JsonResult getMeetingById(String mid, Boolean isAdmin) {
        Users users = MyUtil.getUsers();
        assert users != null;
        Meeting meeting = null;
        if (!isAdmin) {
            // 如果不是管理则从用户参加的会议中查找，找到了则返回成功信息，如果没找到后面还要判断用户是否是房主
            meeting = meetingMapper.getExistMeetWithUser(mid, users.getId());
        }

        if (meeting == null) {
            meeting = meetingMapper.selectById(mid);
            // 如果有管理权限或者是房主则开通
            if (meeting != null && (isAdmin || meeting.getUserId().equals(users.getId()))) {
                return JsonResult.success(meeting);
            }
            return JsonResult.error("找不到该会议，请检查会议号");
        }
        return JsonResult.success(meeting);

    }

    @Override
    public JsonResult selectHadSignInList(String mid, Integer current, Integer size) {
        IPage<Users> usersList = meetingUsersMapper.selectHadSignInList(new Page<>(current, size), mid);
        return JsonResult.success(usersList);
    }

    @Override
    public JsonResult getExistUserList(String mid) {
        return JsonResult.success(sockerSender.getUserListByMid(mid));
    }

    @Override
    public JsonResult delHistoryMeeting(String mid) {
        Integer uid = MyUtil.getUsers().getId();
        Meeting meeting = meetingMapper.selectById(mid);
        Integer i = null;

        // 判断是自己的会议还是参加的会议
        if (meeting.getUserId().equals(uid)) {
            i = meetingMapper.delHistoryMeeting(mid, uid);

        } else {
            i = meetingUsersMapper.delHistoryMeeting(mid, uid);
        }

        if (i != null && i > 0) {
            return JsonResult.success("记录成功删除");
        }
        return JsonResult.error("记录删除失败");
    }

    @Override
    public JsonResult outMeeting(String mid) {
        return joinMeeting(mid, true);
    }

    @Override
    public JsonResult selectMeetingApplications(Integer current, Integer size) {
//        meetingApplicationMapper.selectPage(new Page<>(current,size),new QueryWrapper<>().eq('meeting_id',))
        Integer uid = MyUtil.getUsers().getId();
        IPage<MeetingApplication> meetingApplicationIPage = meetingApplicationMapper.selectMeetingApplications(uid, new Page<>(current, size));
        return JsonResult.success(meetingApplicationIPage);
    }

    @Override
    public JsonResult replyMeetingApplication(String meetingid, Integer userid, Integer result) {
        int update = meetingApplicationMapper.update(
                new MeetingApplication().setState(1),
                new QueryWrapper<MeetingApplication>()
                        .eq("meeting_id", meetingid)
                        .eq("applicant_id", userid)
                        .eq("state", 0)
        );
        if (update == 0) {
            return JsonResult.error("会议查找错误，请检查相关参数");
        }
        if (result == 1) {
            joinMeeting(meetingid, userid);
            // 将会议推送的公告加入到对方的会议通知中
            insertMeetingNoticePushToUser(meetingid, userid);
            // 使用socket发送通知列表
            IPage<MeetingNotice> meetingNoticeIPage = meetingNoticeMapper.selectMeetingNoticePushWithUid(userid, new Page<>(0, 5));
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(meetingNoticeIPage);
            sockerSender.sendInform(jsonObject, 3, userid);
            return JsonResult.success("同意成功");
        } else {
            return JsonResult.success("忽略成功");
        }
    }


    @Override
    public JsonResult selectMeetingNoticePush(Integer current, Integer size) {
        Integer uid = MyUtil.getUsers().getId();
        IPage<MeetingNotice> meetingNoticeIPage = meetingNoticeMapper.selectMeetingNoticePushWithUid(uid, new Page<>(current, size));

        return JsonResult.success(meetingNoticeIPage);
    }

    /**
     * 对方刚加入会议时，将之前的推送公告加入到该用户通知中
     *
     * @param mid
     * @param uid
     */
    private void insertMeetingNoticePushToUser(String mid, Integer uid) {
        meetingNoticeUsersMapper.insertMeetingNoticePushToUser(mid, uid);
    }

    /**
     * 输出该会议参加用户列表
     */
    private List<Users> selectMeetWithParticipantById(String mid) {

        return meetingMapper.selectOneWithParticipantById(mid);
    }

    /**
     * 将该公告推送给该会议下所有成员，并返回刚刚新增数据的id（informId）
     *
     * @param noticeId
     * @param usersList
     */
    private List<Integer> insertMeetingNoticePushToUserList(Integer noticeId, List<Users> usersList) {
        // 如果该公告已经存在，就把该公告已读的删除
        meetingNoticeUsersMapper.delete(new QueryWrapper<MeetingNoticeUsers>()
                .eq("notice_id", noticeId)
                .eq("state", 1)
        );
        List<Integer> informIds = new ArrayList<>();
        for (Users u : usersList) {
            MeetingNoticeUsers meetingNoticeUsers = new MeetingNoticeUsers()
                    .setNoticeId(noticeId)
                    .setUserId(u.getId())
                    .setState(0);
            meetingNoticeUsersMapper.insert(meetingNoticeUsers);
            informIds.add(meetingNoticeUsers.getId());
        }
        return informIds;
    }

    /**
     * 将公告在线发送给该列表中的用户,informId对应数据库中他们的通知消息id
     *
     * @param meetingNotice
     * @param usersList
     */
    private void sendMeetNoticeToAllUser(MeetingNotice meetingNotice, List<Users> usersList, List<Integer> informIds) {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(meetingNotice);
        jsonObject.put("updateTime", jsonObject.getString("updateTime").replace('T', ' '));
        for (int i = 0; i < usersList.size(); i++) {
            jsonObject.put("informId", informIds.get(i));
            sockerSender.sendInform(jsonObject, 3, usersList.get(i).getId());
            try {
                // 睡一下，不然会串线
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendMeetNoticeToAllUser(MeetingNotice meetingNotice) {
        List<Users> usersList = selectMeetWithParticipantById(meetingNotice.getMeetingId());
        // 加入数据库
        List<Integer> informIds = insertMeetingNoticePushToUserList(meetingNotice.getId(), usersList);
        // 加入会议与用户对象
        Meeting meeting = meetingMapper.selectMeetingWithCreaterOne(meetingNotice.getMeetingId());
        Users users = usersMapper.selectById(meetingNotice.getSenderId());
        meetingNotice.setMeeting(meeting);
        meetingNotice.setSender(users);
        // 在线推送
        sendMeetNoticeToAllUser(meetingNotice, usersList, informIds);
    }

    @Override
    public JsonResult addMeetingNotice(MeetingNotice meetingNotice) {
        if (!isBelongUser(meetingNotice.getMeetingId())) {
            return JsonResult.error("添加失败，没有权限");
        }
        meetingNotice.setSenderId(MyUtil.getUsers().getId());
        meetingNotice.setSendTime(LocalDateTime.now());
        meetingNotice.setUpdateTime(LocalDateTime.now());
        int insert = meetingNoticeMapper.insert(meetingNotice);
        // 开启推送
        if (meetingNotice.getType() == 2) {
            sendMeetNoticeToAllUser(meetingNotice);
        }

        return insert > 0 ? JsonResult.success("添加公告成功") : JsonResult.error("添加公告失败");
    }

    @Override
    public JsonResult readMeetinInfrom(Integer informId) {
        int update = meetingNoticeUsersMapper.updateById(
                new MeetingNoticeUsers()
                        .setId(informId)
                        .setState(1)
        );
        return JsonResult.success();
    }

    @Override
    public JsonResult getMeetingNoticeById(String mid, Integer current, Integer size) {
        IPage<MeetingNotice> noticePage = meetingNoticeMapper.selectMeetingNotice(mid, new Page<>(current, size));
        return JsonResult.success(noticePage);
    }

    @Override
    public JsonResult delMeetingNoticeById(String meeingId, Integer noticeId) {
        if (!isBelongUser(meeingId)) {
            return JsonResult.error("删除失败，没有权限");
        }
        int i = meetingNoticeMapper.deleteById(noticeId);
        return i > 0 ? JsonResult.success("删除成功") : JsonResult.error("删除失败");
    }

    @Override
    public JsonResult updateMeetingNotice(MeetingNotice meetingNotice) {
        if (!isBelongUser(meetingNotice.getMeetingId())) {
            return JsonResult.error("编辑失败，没有权限");
        }
        meetingNotice.setSenderId(MyUtil.getUsers().getId());
        meetingNotice.setUpdateTime(LocalDateTime.now());

        // 之前的变为已读
        meetingNoticeUsersMapper.update(new MeetingNoticeUsers().setState(1),
                new QueryWrapper<MeetingNoticeUsers>().eq("notice_id",meetingNotice.getId()));

        int i = meetingNoticeMapper.updateById(meetingNotice);
        // 开启推送
        if (meetingNotice.getType() == 2) {
            sendMeetNoticeToAllUser(meetingNotice);
        }
        return i > 0 ? JsonResult.success("编辑成功") : JsonResult.error("编辑失败");
    }

    @Override
    public JsonResult getMeetingUsersList(String mid, Integer current, Integer size) {
        IPage<MeetingUsers> meetingUsersIPage = meetingUsersMapper.selectMeetingUsersList(new Page<>(current, size), mid);
        return JsonResult.success(meetingUsersIPage);
    }


    /**
     * 发送隐藏会议公告通知给所有参会用户,该公告只能通过通知列表才能看到
     */
    private void sendHiddenMeetNoticeToAllUser(String meetId, String title, String content) {
        MeetingNotice meetingNotice = new MeetingNotice();
        meetingNotice.setMeetingId(meetId);
        meetingNotice.setTitle(title);
        meetingNotice.setContent(content);
        meetingNotice.setSenderId(MyUtil.getUsers().getId());
        meetingNotice.setSendTime(LocalDateTime.now());
        meetingNotice.setUpdateTime(LocalDateTime.now());
        meetingNotice.setHidden(true);

        meetingNoticeMapper.insert(meetingNotice);

        sendMeetNoticeToAllUser(meetingNotice);
    }

    /**
     * 发送隐藏会议公告通知给指定用户,该公告只能通过通知列表才能看到
     */
    private void sendHiddenMeetNoticeToOneUser(String meetId, Integer userId, String title, String content) {
        Meeting meeting = meetingMapper.selectById(meetId);
        Users user = usersMapper.selectById(userId);

        MeetingNotice meetingNotice = new MeetingNotice();
        meetingNotice.setMeetingId(meetId);
        meetingNotice.setMeeting(meeting);
        meetingNotice.setTitle(title);
        meetingNotice.setContent(content);
        meetingNotice.setSenderId(MyUtil.getUsers().getId());
        meetingNotice.setSender(MyUtil.getUsers());
        meetingNotice.setSendTime(LocalDateTime.now());
        meetingNotice.setUpdateTime(LocalDateTime.now());
        meetingNotice.setHidden(true);

        meetingNoticeMapper.insert(meetingNotice);
        List<Users> list = new ArrayList<>();
        list.add(user);

        List<Integer> informIds = insertMeetingNoticePushToUserList(meetingNotice.getId(), list);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(meetingNotice);
        jsonObject.put("updateTime", jsonObject.getString("updateTime").replace('T', ' '));
        jsonObject.put("informId", informIds.get(0));
        sockerSender.sendInform(jsonObject, 3, userId);
    }

    @Override
    public JsonResult delMeetingUserById(String mid, Integer userId) {
        if (!isBelongUser(mid)) {
            return JsonResult.error("踢出失败，没有权限");
        }
        // 删除用户与会议的关系
        meetingUsersMapper.delete(
                new QueryWrapper<MeetingUsers>()
                        .eq("users_id", userId)
                        .eq("meeting_id", mid)
        );
        Meeting meeting = meetingMapper.selectById(mid);
        // 发送踢人通知单独给该用户
        sendHiddenMeetNoticeToOneUser(mid, userId, String.format("你已被踢出 %s 会议", meeting.getName()),
                String.format("你已被踢出 %s 会议<br>会议号：%s", meeting.getName(), meeting.getId()));
        // 通知会议socket，如果此人在会议里就自动刷新，因为之前关系删掉后，刷新就会到404
        sockerSender.sendActionToMeetUser("refresh", mid, userId, "你已被踢出会议", false);

        return JsonResult.success("成功踢出会议");
    }

    @Override
    public JsonResult getMeetingUsersStateById(String mid, Integer uid) {
        MeetingUsers meetingUsers = meetingUsersMapper.selectOne(
                new QueryWrapper<MeetingUsers>()
                        .eq("meeting_id", mid)
                        .eq("users_id", uid)
        );

        return JsonResult.success(meetingUsers);
    }

    @Override
    public JsonResult setMeetingUsersStateByIdMany(String mid, JSONObject jsonObject) {

        List<Map<String, Object>> list = (List<Map<String, Object>>) jsonObject.get("list");


        for (Map<String, Object> map : list) {
            QueryWrapper<MeetingUsers> wrapper = new QueryWrapper<MeetingUsers>()
                    .eq("meeting_id", mid).eq("users_id", map.get("userId"));
            MeetingUsers meetingUsers = new MeetingUsers()
                    .setHadMuted((Boolean) map.get("hadMuted")).setHadBanup((Boolean) map.get("hadBanup"));
            if (meetingUsers.getHadMuted()) {
                sockerSender.sendActionToMeetUser("stop_speech", mid, (Integer) map.get("userId"), "你已被禁止开麦", false);
            }
            if (meetingUsers.getHadBanup()) {
                sockerSender.sendActionToMeetUser("stop_up", mid, (Integer) map.get("userId"), "你已被禁止投屏", false);
            }
            meetingUsersMapper.update(meetingUsers, wrapper);
        }


        return JsonResult.success("修改成功");
    }

    @Override
    public JsonResult setMeetingUsersStateByIdOne(String mid, Integer uid, JSONObject jsonObject) {


        Boolean uping = jsonObject.getBoolean("uping");
        Boolean speeching = jsonObject.getBoolean("speeching");

        QueryWrapper<MeetingUsers> wrapper = new QueryWrapper<MeetingUsers>().eq("meeting_id", mid).eq("users_id", uid);

        // 没有找到该数据，再检测是否是支持人是的话，就插入一条
        if (meetingUsersMapper.selectOne(wrapper) == null && meetingMapper.selectById(mid).getUserId().equals(uid)) {
            meetingUsersMapper.insert(new MeetingUsers().setMeetingId(mid).setUsersId(uid).setIsFounder(true));
        }


        if (uping != null) {
            meetingUsersMapper.update(new MeetingUsers().setUping(uping), wrapper);
        } else if (speeching != null) {
            meetingUsersMapper.update(new MeetingUsers().setSpeeching(speeching), wrapper);
        } else {
            return JsonResult.error("修改失败");
        }
        sockerSender.sendStateToMeetUser(mid, uid, uping, speeching);
        return JsonResult.success();
    }


}
