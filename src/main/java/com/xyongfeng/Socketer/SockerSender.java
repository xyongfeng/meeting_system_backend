package com.xyongfeng.Socketer;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SockerSender {

//    private final int WAY_FRIENDAPPLY = 1, WAY_MEETAPPLY = 2, WAY_MEETINFORM = 3;

    /**
     * 发送通知消息给对面
     * 其中way参数 1代表好友申请通知，2代表会议申请通知，3代表会议公告通知
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

    private void sendMessage(String event, JSONObject data) {
        Integer toId = data.getInteger("toId");
        for (SocketUser socketUser : SocketState.CONNECT_USERS_MAP.values()) {
            if (socketUser.users.getId().equals(toId)) {
                socketUser.client.sendEvent(event, data);
                break;
            }
        }
    }
}
