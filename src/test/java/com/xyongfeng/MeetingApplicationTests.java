package com.xyongfeng;

import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.service.AdminsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@SpringBootTest
class MeetingApplicationTests {

    @Resource
    private AdminsMapper adminsMapper;
    @Resource
    private AdminsService adminsService;
    @Test
    void test1(){
//        List<Admins> admins = adminsMapper.selectList(null);
//        admins.forEach(System.out::println);
    }
    @Test
    void test2(){
        log.info("test2");
        List<Admins> admins = adminsService.listPage(new MyPage(0,3));
        admins.forEach(System.out::println);
    }

}
