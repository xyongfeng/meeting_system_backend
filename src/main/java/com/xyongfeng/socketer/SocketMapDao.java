package com.xyongfeng.socketer;

import com.xyongfeng.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SocketMapDao {

    @Autowired
    private RedisUtil redisUtil;

    public void putByUUID(UUID uuid, SocketUser value) {
        SocketState.CONNECT_USERS_MAP_UUID.put(uuid, value);
    }

    public void deleteByUUID(UUID uuid) {
        SocketState.CONNECT_USERS_MAP_UUID.remove(uuid);
    }

    public SocketUser getByUUID(UUID uuid) {
        return SocketState.CONNECT_USERS_MAP_UUID.get(uuid);
    }

    public void putByUserId(Integer userId, SocketUser value) {
//        redisTemplate.opsForHash().put(SOCKET_BY_USERID, userId.toString(), value);
        SocketState.CONNECT_USERS_MAP_USERID.put(userId, value);
    }

    public void deleteByUserId(Integer userId) {
//        redisTemplate.opsForHash().delete(SOCKET_BY_USERID, userId.toString());
        SocketState.CONNECT_USERS_MAP_USERID.remove(userId);
    }

    public SocketUser getByUserId(Integer userId) {
//        return redisTemplate.opsForHash().get(SOCKET_BY_USERID, userId.toString());
        return SocketState.CONNECT_USERS_MAP_USERID.get(userId);
    }

    public void putSocketUser(UUID uuid, Integer userId, SocketUser socketUser) {
        putByUUID(uuid, socketUser);
        putByUserId(userId, socketUser);
    }

    public SocketUser getSocketUserByUUID(UUID uuid) {
        return getByUUID(uuid);
    }

    public SocketUser getSocketUserByUserId(Integer userId) {
        return getByUserId(userId);

    }

    private void deleteSocketCommon(UUID uuid, Integer userId) {
        deleteByUUID(uuid);
        deleteByUserId(userId);

        // 从redis登录移除
        redisUtil.deleteUserOnline(userId);
    }

    public void deleteSocketByUUID(UUID uuid) {
        Integer userId = getSocketUserByUUID(uuid).getUsers().getId();
        deleteSocketCommon(uuid, userId);
    }

    public void deleteSocketByUserId(Integer userId) {
        UUID uuid = getSocketUserByUserId(userId).getClient().getSessionId();
        deleteSocketCommon(uuid, userId);
    }

    public int getSize() {
        int sizeByUUID = SocketState.CONNECT_USERS_MAP_UUID.size();
        int sizeByUserId = SocketState.CONNECT_USERS_MAP_USERID.size();

        return Math.max(sizeByUUID, sizeByUserId);

    }
}
