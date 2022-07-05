package com.xyongfeng;

import com.xyongfeng.mapper.MeetingMapper;
import com.xyongfeng.mapper.MenuMapper;
import com.xyongfeng.mapper.UsersMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.service.MeetingService;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class MeetingApplicationTests {

    @Resource
    private UsersMapper usersMapper;
    @Resource
    private UsersService adminsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleService roleService;

    @Autowired
    private MeetingService meetingService;

    @Test
    void test1() {
        List<Users> admins = usersMapper.selectList(null);
        admins.forEach(System.out::println);
    }

    @Test
    void test2() {
        log.info("test2");
        List<Users> admins = adminsService.listPage(new MyPage(0, 3));
        admins.forEach(System.out::println);
    }
//    $2a$10$XXwc8A48H3gHxnGKQ7T.aeRn4hkyrB48UTrqR67X3V567jL9mCDGi

    @Test
    void test3() {
        System.out.println(passwordEncoder.encode("123"));
    }

    @Test
    void test4() {
        List<Menu> menus = menuMapper.getMenusByUserId(1);
        System.out.println(menus);
    }

    @Test
    void test5(){
        List<Role> roles = roleService.selectRoleWithUserid(1);
        System.out.println(roles);
    }

    @Test
    void test6(){
        List<Meeting> meetings = meetingService.listPage(new MyPage(1, 3));
        System.out.println(meetings);
    }

    @Test
    void test7(){
        List<Meeting> meetings = meetingService.listPageByUserid(new MyPage(1, 3),2);
        System.out.println(meetings);
    }
}
