package com.xyongfeng.Socketer;

import com.corundumstudio.socketio.SocketIOClient;
import com.xyongfeng.pojo.Users;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SocketUser {
    SocketIOClient client;
    Users users;
    List<String> meetings;
}