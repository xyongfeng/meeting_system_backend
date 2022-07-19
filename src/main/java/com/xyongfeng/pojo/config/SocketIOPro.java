package com.xyongfeng.pojo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "socketio")
@Data
public class SocketIOPro {
    public String host;

    public Integer port;

    public int bossCount;

    public int workCount;

    public boolean allowCustomRequests;

    public int upgradeTimeout;

    public int pingTimeout;

    public int pingInterval;


}
