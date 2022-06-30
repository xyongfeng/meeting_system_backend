package com.xyongfeng;

import com.xyongfeng.mapper.AdminsMapper;
import com.xyongfeng.mapper.MenuMapper;
import com.xyongfeng.pojo.Admins;
import com.xyongfeng.pojo.Menu;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.service.AdminsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class MeetingApplicationTests {

    @Resource
    private AdminsMapper adminsMapper;
    @Resource
    private AdminsService adminsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MenuMapper menuMapper;
    @Test
    void test1() {
        List<Admins> admins = adminsMapper.selectList(null);
        admins.forEach(System.out::println);
    }

    @Test
    void test2() {
        log.info("test2");
        List<Admins> admins = adminsService.listPage(new MyPage(0, 3));
        admins.forEach(System.out::println);
    }
//    $2a$10$XXwc8A48H3gHxnGKQ7T.aeRn4hkyrB48UTrqR67X3V567jL9mCDGi

    @Test
    void test3() {
        System.out.println(passwordEncoder.encode("123"));
    }

    @Test
    void test4() {
        List<Menu> menus = menuMapper.getMenusByAdminId(1);
        System.out.println(menus);
    }
}
