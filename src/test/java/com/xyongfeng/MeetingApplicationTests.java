package com.xyongfeng;

import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.pojo.Admins;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class MeetingApplicationTests {

    @Resource
    private AdminsMapper adminsMapper;
    @Test
    void test1(){
        List<Admins> admins = adminsMapper.selectAll();
        System.out.println(admins);
    }
}
