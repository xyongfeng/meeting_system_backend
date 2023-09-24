package com.xyongfeng;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MeetingApplication implements CommandLineRunner {

    @Autowired
    private SocketIOServer socketIOServer;

    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        log.info("socket.io启动");
    }
}
