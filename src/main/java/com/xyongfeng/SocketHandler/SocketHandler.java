package com.xyongfeng.SocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import com.xyongfeng.pojo.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class SocketHandler {
    @Autowired
    private SocketIOServer socketIoServer;
    /**
     * 连接上的用户公共房间
     */
    private final static String CONNECTROOMID = "CONNECTED";
    @AllArgsConstructor
    private static class SocketUser{
        SocketIOClient client;
        Users users;
        List<String> meetings;
    }
    /**
     * 用户sessionId对应用户信息的map
     */
    public static ConcurrentMap<UUID, SocketUser> connectUsersMap = new ConcurrentHashMap<>();

    /**
     * 监听连接
     * @param client
     */

    @OnConnect
    public void onConnectEvent(SocketIOClient client){
        log.info(String.format("连接成功 %s",client.getSessionId()));
    }

    /**
     * 用户断开连接要删除信息
     * @param client
     */
    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client){
        // 更新房间的人数
        Users users = connectUsersMap.get(client.getSessionId()).users;
        connectUsersMap.get(client.getSessionId()).meetings.forEach(mid->sendMeetWithout(client,mid,"left_user",users.getId()));

        connectUsersMap.remove(client.getSessionId());
        log.info(String.format("断开连接：%s",client.getSessionId())+ " "+connectUsersMap.size());
    }
    /**
     * 接收连接人信息
     * @param client
     * @param users
     */
    @OnEvent("info")
    public void onInfoEvent(SocketIOClient client, Users users){
        // 加入连接用户列表
        connectUsersMap.put(client.getSessionId(),new SocketUser(client,users,new ArrayList<>()));
        // 加入用户公共房间
        client.joinRoom(CONNECTROOMID);

        log.info("info：" + users);
    }
    private void sendMeetchatBySys(String meetingId,String msg){
        Users user = new Users().setId(-1).setName("sys");
        JSONObject data = new JSONObject();
        data.put("msg",msg);
        sendMeetchat(meetingId,user,data);

    }
    private void sendMeetchat(String meetingId,Users users, JSONObject data){
        data.put("user",users);
        socketIoServer.getRoomOperations(meetingId).sendEvent("meetchat", data);
    }
    /**
     * 房间消息
     * @param client
     * @param data
     */
    @OnEvent("meetchat")
    public void onMeetchatEvent(SocketIOClient client, JSONObject data){
        String meetingId = data.getString("meetingId");
        Users users = connectUsersMap.get(client.getSessionId()).users;
        sendMeetchat(meetingId,users,data);
        log.info("meetchat：" + data);
    }

    /**
     * 请求加入会议
     * @param client
     * @param data
     */
    @OnEvent("join")
    public void onJoinRoomEvent(SocketIOClient client, JSONObject data){
        String meetingId = data.getString("meetingId");
        // 如果存在
        if(isExist(meetingId,client)){
            sendUserListByid(meetingId,getUserIdBySess(client.getSessionId()));
            return;
        }
        // 新加入的用户信息
        Users joinedUser = connectUsersMap.get(client.getSessionId()).users;
        // 告诉客户端已经加入可以进行sdp交换了
        JSONObject res = new JSONObject();
        res.put("type","joined");
        res.put("meetingId",meetingId);
        client.sendEvent("message",res );
        // 向房间里的人发送加入消息
        socketIoServer.getRoomOperations(meetingId).sendEvent("joined_user",joinedUser);
        sendMeetchatBySys(meetingId,joinedUser.getName().concat("加入房间"));
        // 记录此人加入的房间
        connectUsersMap.get(client.getSessionId()).meetings.add(meetingId);
        client.joinRoom(meetingId);
        sendUserListByid(meetingId,joinedUser.getId());
        log.info(String.format("%s 加入房间 %d",joinedUser.getName(),socketIoServer.getRoomOperations(meetingId).getClients().size()));
    }
    private Integer getUserIdBySess(UUID sessionId){
        return connectUsersMap.get(sessionId).users.getId();
    }
    private List<Users> getUserListByMid(String meetingId){
        List<Users> usersList = new ArrayList<>();
        // 把房间里的用户通过sessionid找到信息，并加入users列表
        socketIoServer.getRoomOperations(meetingId).getClients().forEach(x->usersList.add(connectUsersMap.get(x.getSessionId()).users));
        return usersList;
    }

    private void sendUserListByid(String meetingId,Integer userId) {
        List<Users> usersList = getUserListByMid(meetingId);
        // 将房间用户信息发送给刚加入的人
        sendMeetById(userId,meetingId,"meeting_users",usersList);
    }

    private void sendUserListWithout(String meetingId,SocketIOClient client) {
        List<Users> usersList = getUserListByMid(meetingId);
        // 将房间用户信息发送给刚加入的人
        sendMeetWithout(client,meetingId,"meeting_users",usersList);
    }

    @OnEvent("leave")
    public void onLeaveRoomEvent(SocketIOClient client, JSONObject data){
        String meetingId = data.getString("meetingId");
        client.leaveRoom(meetingId);
        sendMeetWithout(client,meetingId,"left_user",getUserIdBySess(client.getSessionId()));
        log.info(String.format("%s 离开房间 %d",connectUsersMap.get(client.getSessionId()).users.getName(),socketIoServer.getRoomOperations(meetingId).getClients().size()));
    }
    /**
     * WebRtc建立通话
     * @param client
     * @param data
     */
    @OnEvent("message")
    public void onMesssageEvent(SocketIOClient client, JSONObject data){
        String meetingId = data.getString("meetingId");
        Integer toId = data.getInteger("toId");
        if(toId != null){
            sendMeetById(toId,meetingId,"message",data);
        }else{
            sendMeetWithout(client,meetingId,"message",data);
        }
        log.info(String.format("Messsage：%s", data));
    }

    /**
     * 发送给该房间该id的人
     * @param userId
     * @param meetingId
     * @param event
     * @param data
     */
    private void sendMeetById(Integer userId,String meetingId,String event,Object data){
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for(SocketIOClient c:clients){
            if(connectUsersMap.get(c.getSessionId()).users.getId().equals(userId)) {
                c.sendEvent(event,data);
                return;
            }
        }

    }
    /**
     * 发给房间里的其他人
     */
    private void sendMeetWithout(SocketIOClient client,String meetingId,String event,Object data){
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();

        for(SocketIOClient c:clients){
            if(c.getSessionId() != client.getSessionId()) {
                c.sendEvent(event,data);
            }
        }
    }

    /**
     * 是否已经存在于会议
     * @param meetingId
     * @param client
     * @return
     */
    private boolean isExist(String meetingId,SocketIOClient client){
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingId).getClients();
        for(SocketIOClient c:clients){
            if(c.getSessionId() == client.getSessionId()) {
                return true;
            }
        }
        return false;
    }
}
