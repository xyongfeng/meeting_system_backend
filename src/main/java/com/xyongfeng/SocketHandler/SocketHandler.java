package com.xyongfeng.SocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class SocketHandler {
    @Autowired
    private SocketIOServer socketIoServer;
    /**
     * 当前连接的用户
     */
    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    /**
     * 监听连接
     * @param client
     */
    @OnConnect
    public void onConnectEvent(SocketIOClient client){
        client.sendEvent("news", "conn ok");
        log.info(String.format("连接成功 %s",client.getSessionId()));
    }

    @OnDisconnect
    public void onDisconnectEvent(SocketIOClient client){
        log.info(String.format("断开连接：%s",client.getSessionId()));
    }

    @OnEvent("news")
    public void onNewsEvent(SocketIOClient client, JSONObject data){
        client.sendEvent("news", data);
        log.info("发来消息：" + data);
    }

    @OnEvent("join")
    public void onJoinRoomEvent(SocketIOClient client, JSONObject data){

        String meetingID = data.getString("meetingID");
        String name = data.getString("name");
        // 如果存在
        if(isExist(meetingID,client)){
            return;
        }

        client.sendEvent("message", "joined");
        client.joinRoom(meetingID);
        socketIOClientMap.put(name,client);
        socketIoServer.getRoomOperations(meetingID).sendEvent("news",name + "加入房间");
        log.info(String.valueOf(socketIoServer.getRoomOperations(meetingID).getClients().size()));
    }
    @OnEvent("message")
    public void onMesssageEvent(SocketIOClient client, JSONObject data){
        String meetingID = data.getString("meetingID");
        sendMeetWithout(client,meetingID,"message",data);
        log.info(String.format("Messsage：%s", data));
    }

    /**
     * 发给房间里的其他人
     */
    private void sendMeetWithout(SocketIOClient client,String meetingID,String event,Object data){
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingID).getClients();
        for(SocketIOClient c:clients){
            if(c.getSessionId() != client.getSessionId()) {
                c.sendEvent(event,data);
            }
        }

    }

    private boolean isExist(String meetingID,SocketIOClient client){
        Collection<SocketIOClient> clients = socketIoServer.getRoomOperations(meetingID).getClients();
        for(SocketIOClient c:clients){
            if(c.getSessionId() == client.getSessionId()) {
                return true;
            }
        }
        return false;
    }
}
