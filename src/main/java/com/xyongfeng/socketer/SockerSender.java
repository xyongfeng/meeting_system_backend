package com.xyongfeng.socketer;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.ChatFilterService;
import com.xyongfeng.service.MeetingChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class SockerSender {
    //    private final int WAY_FRIENDAPPLY = 1, WAY_MEETAPPLY = 2, WAY_MEETINFORM = 3;
    @Autowired
    private SocketIOServer socketIoServer;

    @Autowired
    private ChatFilterService chatFilterService;

    @Autowired
    private MeetingChatService meetingChatService;

    @Autowired
    private SocketMapDao socketMapDao;

    /**
     * 发送执行指令给会议某用户执行
     *
     * @param actionType refresh,stop_up,stop_speech
     * @param meetId
     * @param userId
     * @param msg
     * @param without    真为 发送给该id以外的人，假为 只发送给id
     */
    public void sendActionToMeetUser(String actionType, String meetId, Integer userId, String msg, boolean without) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", actionType);
        map.put("msg", msg);
        if (without) {
            sendMeetWithoutId(userId, meetId, "action", map);
        } else {
            sendMeetById(userId, meetId, "action", map);
        }
    }

    /**
     * 发送用户的开麦投屏状态给房间所有人包括自己
     *
     * @param meetId
     * @param userId
     * @param uping
     * @param speeching
     */
    public void sendStateToMeetUser(String meetId, Integer userId, Boolean uping, Boolean speeching) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("uping", uping);
        map.put("speeching", speeching);
        sendMeetAll(meetId, "updateState", map);
    }

    /**
     * 发送通知消息给对面
     *
     * @param data
     * @param way  1代表好友申请通知，2代表会议申请通知，3代表会议公告通知
     * @param toId
     */
    public void sendInform(JSONObject data, Integer way, Integer toId) {
        data.put("toId", toId);
        sendInform(data, way);
    }

    public void sendInform(JSONObject data, Integer way) {
        data.put("way", way);
        sendMessage("updateInform", data);
    }

    /**
     * 将聊天内容并发送到对面
     */
    public void sendChat(JSONObject data) {

        sendMessage("friendChat", data);
    }

    /**
     * 对用户进行消息发送
     *
     * @param event
     * @param data
     */
    public void sendMessage(String event, JSONObject data) {
        Integer toId = data.getInteger("toId");
        SocketUser socketUser = socketMapDao.getSocketUserByUserId(toId);

        log.info(String.format("socket:事件: %s,sessionId:%s,对象：%s，data：%s", event, socketUser.client.getSessionId(), socketUser.users, data));
        socketUser.client.sendEvent(event, data);

//        for (SocketUser socketUser : SocketState.CONNECT_USERS_MAP.values()) {
//            if (socketUser.users.getId().equals(toId)) {
//                log.info(String.format("socket:事件: %s,sessionId:%s,对象：%s，data：%s", event, socketUser.client.getSessionId(), socketUser.users, data));
//                socketUser.client.sendEvent(event, data);
//
//            }
//        }
    }

    /**
     * 以系统的口吻向聊天框发送消息
     *
     * @param meetingId
     * @param msg
     */
    public void sendMeetchatBySys(String meetingId, String msg) {
        Users user = new Users().setId(-1).setName("sys");
        JSONObject data = new JSONObject();
        data.put("msg", msg);
        sendMeetchat(meetingId, user, data);

    }

    /**
     * 向聊天室发送消息
     *
     * @param meetingId
     * @param users
     * @param data
     */
    public void sendMeetchat(String meetingId, Users users, JSONObject data) {
        data.put("user", users);
        String msg = chatFilterService.filter(data.getString("msg"));
        data.put("msg", msg);
        if (users.getId() > 0) {
            meetingChatService.insert(meetingId, users.getId(), msg);
        }
        socketIoServer.getRoomOperations(meetingId).sendEvent("meetchat", data);
    }

    /**
     * 通过sessionId获取用户Id
     *
     * @param sessionId
     * @return
     */
    public Integer getUserIdBySess(UUID sessionId) {

//        return SocketState.CONNECT_USERS_MAP.get(sessionId).users.getId();
        return socketMapDao.getSocketUserByUUID(sessionId).users.getId();
    }

    /**
     * 通过会议Id获取当前房间的用户列表
     *
     * @param meetingId
     * @return
     */
    public List<Users> getUserListByMid(String meetingId) {
        List<Users> usersList = new ArrayList<>();
        // 把房间里的用户通过sessionid找到信息，并加入users列表
//        socketIoServer.getRoomOperations(meetingId).getClients().forEach(x -> usersList.add(
//                SocketState.CONNECT_USERS_MAP.get(x.getSessionId()).users));
        socketIoServer.getRoomOperations(meetingId).getClients().forEach(x -> usersList.add(
                socketMapDao.getSocketUserByUUID(x.getSessionId()).users));
        return usersList;
    }

    /**
     * 将房间用户信息发送给刚加入的人
     *
     * @param meetingId
     * @param userId
     */
    public void sendUserListByid(String meetingId, Integer userId) {
        List<Users> usersList = getUserListByMid(meetingId);
        sendMeetById(userId, meetingId, "meeting_users", usersList);
    }

    /**
     * 将房间用户信息发送给其他人
     *
     * @param meetingId
     * @param client
     */
    public void sendUserListWithout(String meetingId, SocketIOClient client) {
        List<Users> usersList = getUserListByMid(meetingId);
        sendMeetWithout(client, meetingId, "meeting_users", usersList);
    }


    /**
     * 发送给该房间中的所有人
     *
     * @param meetingId
     * @param event
     * @param data
     */
    public void sendMeetAll(String meetingId, String event, Object data) {
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for (SocketIOClient c : clients) {

            c.sendEvent(event, data);

        }
    }


    /**
     * 发送给该房间中该id的人
     *
     * @param userId
     * @param meetingId
     * @param event
     * @param data
     */
    public void sendMeetById(Integer userId, String meetingId, String event, Object data) {
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for (SocketIOClient c : clients) {
            if (socketMapDao.getSocketUserByUUID(c.getSessionId()).users.getId().equals(userId)) {
                c.sendEvent(event, data);
                return;
            }
        }
    }

    /**
     * 发送给该房间中除此id以外的人
     *
     * @param userId
     * @param meetingId
     * @param event
     * @param data
     */
    public void sendMeetWithoutId(Integer userId, String meetingId, String event, Object data) {
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for (SocketIOClient c : clients) {
            if (!socketMapDao.getSocketUserByUUID(c.getSessionId()).users.getId().equals(userId)) {
                c.sendEvent(event, data);

            }
        }
    }

    /**
     * 发给房间里的其他人
     */
    public void sendMeetWithout(SocketIOClient client, String meetingId, String event, Object data) {
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();

        for (SocketIOClient c : clients) {
            if (c.getSessionId() != client.getSessionId()) {
                c.sendEvent(event, data);
            }
        }
    }

    /**
     * 是否已经存在于会议
     *
     * @param meetingId
     * @param client
     * @return
     */
    public boolean isExist(String meetingId, SocketIOClient client) {
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for (SocketIOClient c : clients) {
            if (c.getSessionId() == client.getSessionId()) {
                return true;
            }
        }
        return false;
    }
}
