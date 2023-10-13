package com.xyongfeng.socketer;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import com.xyongfeng.mapper.MeetingUsersMapper;
import com.xyongfeng.pojo.MeetingUsers;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
@Slf4j
public class SocketHandler {

    @Autowired
    private SockerSender sockerSender;
    @Autowired
    private MeetingUsersMapper meetingUsersMapper;
    @Autowired
    private SocketIOServer socketIoServer;
    @Autowired
    private SocketMapDao socketMapDao;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 连接上的用户公共房间
     */
    private final static String CONNECTROOMID = "CONNECTED";

    /**
     * 监听连接
     *
     * @param client
     */

    @OnConnect
    public void onConnectEvent(SocketIOClient client) {
        log.info(String.format("连接成功 %s", client.getSessionId()));
    }

    /**
     * 用户断开连接要删除信息
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client) {

//        SocketUser socketUser = SocketState.CONNECT_USERS_MAP.get(client.getSessionId());
        SocketUser socketUser = socketMapDao.getSocketUserByUUID(client.getSessionId());
        if (socketUser == null) {
            return;
        }
        // 更新房间的人数
        Users users = socketUser.users;
        // 每个房间取消投屏，开麦
        socketUser.meetings.forEach(
                mid -> meetingUsersMapper.update(new MeetingUsers().setSpeeching(false).setUping(false),
                        new QueryWrapper<MeetingUsers>()
                                .eq("meeting_id_xq", mid)
                                .eq("users_id_xq", users.getId()))
        );

        // 对进入的每个房间，告诉其他人，他走了
        socketUser.meetings.forEach(mid -> sockerSender.sendMeetWithout(client, mid, "left_user", users.getId()));
        // 连接信息删除
//        SocketState.CONNECT_USERS_MAP.remove(client.getSessionId());
        socketMapDao.deleteSocketByUUID(client.getSessionId());
        log.info(String.format("断开连接：%s", client.getSessionId()).concat(" ").concat(String.valueOf(socketMapDao.getSize())));
    }

    /**
     * 接收连接人信息
     *
     * @param client
     * @param users
     */
    @OnEvent("info")
    public void onInfoEvent(SocketIOClient client, Users users) {

//        redisUtil.putUserOnline(users.getId(),);

        // 加入连接用户列表
        SocketUser socketUser = new SocketUser(client, users, new ArrayList<>());
        socketMapDao.putSocketUser(client.getSessionId(), users.getId(), socketUser);

        // 加入用户公共房间
        client.joinRoom(CONNECTROOMID);


        log.info(String.format("info：%s", users));
    }


    /**
     * 房间消息
     *
     * @param client
     * @param data
     */
    @OnEvent("meetchat")
    public void onMeetchatEvent(SocketIOClient client, JSONObject data) {
        String meetingId = data.getString("meetingId");
//        Users users = SocketState.CONNECT_USERS_MAP.get(client.getSessionId()).users;
        Users users = socketMapDao.getSocketUserByUUID(client.getSessionId()).users;

        sockerSender.sendMeetchat(meetingId, users, data);
        log.info(String.format("meetchat：%s", data));
    }

    /**
     * 请求加入会议
     *
     * @param client
     * @param data
     */
    @OnEvent("join")
    public void onJoinRoomEvent(SocketIOClient client, JSONObject data) {
        String meetingId = data.getString("meetingId");
        // 如果存在
        if (sockerSender.isExist(meetingId, client)) {
            sockerSender.sendUserListByid(meetingId, sockerSender.getUserIdBySess(client.getSessionId()));
            return;
        }

        // 新加入的用户信息
//        Users joinedUser = SocketState.CONNECT_USERS_MAP.get(client.getSessionId()).users;
        SocketUser socketUser = socketMapDao.getSocketUserByUUID(client.getSessionId());
        Users joinedUser = socketUser.users;
        // 告诉客户端已经加入可以进行sdp交换了
        JSONObject res = new JSONObject();
        res.put("type", "joined");
        res.put("meetingId", meetingId);
        client.sendEvent("message", res);
        // 向房间里的人发送加入消息
        socketIoServer.getRoomOperations(meetingId).sendEvent("joined_user", joinedUser);
        sockerSender.sendMeetchatBySys(meetingId, joinedUser.getName().concat("加入房间"));
        // 记录此人加入的房间
        // SocketState.CONNECT_USERS_MAP.get(client.getSessionId()).meetings.add(meetingId);

        socketUser.getMeetings().add(meetingId);
        socketMapDao.putSocketUser(socketUser.getClient().getSessionId(), socketUser.getUsers().getId(), socketUser);

        client.joinRoom(meetingId);
        sockerSender.sendUserListByid(meetingId, joinedUser.getId());
        log.info(String.format("%s 加入房间 %d", joinedUser.getName(), socketIoServer.getRoomOperations(meetingId).getClients().size()));
    }


    @OnEvent("leave")
    public void onLeaveRoomEvent(SocketIOClient client, JSONObject data) {
        String meetingId = data.getString("meetingId");
        client.leaveRoom(meetingId);
        sockerSender.sendMeetWithout(client, meetingId, "left_user", sockerSender.getUserIdBySess(client.getSessionId()));
//        String name = SocketState.CONNECT_USERS_MAP.get(client.getSessionId()).users.getName();
        String name = socketMapDao.getSocketUserByUUID(client.getSessionId()).users.getName();
        log.info(String.format("%s 离开房间 %d", name, socketIoServer.getRoomOperations(meetingId).getClients().size()));
    }

    /**
     * WebRtc建立通话
     *
     * @param client
     * @param data
     */
    @OnEvent("message")
    public void onMesssageEvent(SocketIOClient client, JSONObject data) {
        String meetingId = data.getString("meetingId");
        Integer toId = data.getInteger("toId");
        if (toId != null) {
            sockerSender.sendMeetById(toId, meetingId, "message", data);
        } else {
            sockerSender.sendMeetWithout(client, meetingId, "message", data);
        }
        log.info(String.format("Messsage：%s", data));
    }


    /**
     * 会议存在心跳，每发送一次，存在时长就加上1分钟
     *
     * @param client
     * @param data
     */
    @OnEvent("exist_minute")
    private void onExistMinuteEvent(SocketIOClient client, JSONObject data) {
        log.info(String.format("exist_minute：%s", data));
        String meetingId = data.getString("meetingId");
        Integer userId = sockerSender.getUserIdBySess(client.getSessionId());
//        meetingUsersMapper.addExistMinute(meetingId,userId);
        MeetingUsers meetingUsers = meetingUsersMapper.selectOne(new QueryWrapper<MeetingUsers>()
                .eq("meeting_id_xq", meetingId)
                .eq("users_id_xq", userId));
        if (meetingUsers == null) {
            return;
        }
        Integer existMinute = meetingUsers.getExistMinute();
        meetingUsersMapper.update(meetingUsers.setExistMinute(existMinute + 1)
                , new QueryWrapper<MeetingUsers>()
                        .eq("meeting_id_xq", meetingId)
                        .eq("users_id_xq", userId));
    }

}
