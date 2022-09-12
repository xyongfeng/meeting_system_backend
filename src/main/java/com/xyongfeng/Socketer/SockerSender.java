package com.xyongfeng.Socketer;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SockerSender {

    private final int WAY_FRIENDAPPLY = 1, WAY_MEETAPPLY = 2, WAY_MEETINFORM = 3;

    /**
     * 告诉用户有新通知接收
     * @param toId 接收人
     * @param way  1 好友申请 2 会议申请 3 会议通知
     */
    public void addInform(int toId, int way) {
        for (SocketUser socketUser : SocketState.CONNECT_USERS_MAP.values()) {
            if (socketUser.users.getId() == toId) {
                socketUser.client.sendEvent("updateInform", way);
                break;
            }
        }
    }

    /**
     * 将聊天内容并发送到对面
     */
    public void sendChat(JSONObject data) {
        Integer toId = data.getInteger("toId");
        for (SocketUser socketUser : SocketState.CONNECT_USERS_MAP.values()) {
            if (socketUser.users.getId().equals(toId)) {
                socketUser.client.sendEvent("friendChat", data);
                break;
            }
        }
    }

}
