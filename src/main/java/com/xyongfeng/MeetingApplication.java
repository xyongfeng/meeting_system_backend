package com.xyongfeng;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableRedisHttpSession
@Slf4j
public class MeetingApplication implements CommandLineRunner {

    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        socketIOServer.start();
        log.info("socket.io启动");

        // 对每个key去除前缀 meeting_system 长度为15
        Set<String> keys = redisTemplate.keys("*").stream().map(x -> x.substring(15)).collect(Collectors.toSet());
        if (!keys.isEmpty()) {
            Long result = redisTemplate.delete(keys);
            log.info("redis缓存清空");
        }

    }
}
