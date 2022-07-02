package com.xyongfeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeetingApplication {

    public static void main(String[] args) {

        SpringApplication.run(MeetingApplication.class, args);
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
    }

}
