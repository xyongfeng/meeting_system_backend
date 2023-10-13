package com.xyongfeng.socketer;

import com.corundumstudio.socketio.SocketIOClient;
import com.xyongfeng.pojo.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data

public class SocketUser{
    SocketIOClient client;
    Users users;
    List<String> meetings;

}