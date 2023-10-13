package com.xyongfeng.socketer;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class SocketState {

    /**
     * 用户sessionId对应用户信息的map
     */
    public static final ConcurrentMap<UUID, SocketUser> CONNECT_USERS_MAP_UUID = new ConcurrentHashMap<>();

    public static final ConcurrentMap<Integer, SocketUser> CONNECT_USERS_MAP_USERID = new ConcurrentHashMap<>();


}
