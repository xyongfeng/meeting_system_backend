package com.xyongfeng;

import com.xyongfeng.mapper.MenuMapper;
import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.pojo.Menu;
import com.xyongfeng.pojo.MyPage;
import com.xyongfeng.pojo.Role;
import com.xyongfeng.pojo.Users;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
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
    private UserMapper usersMapper;
    @Resource
    private UsersService adminsService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleService roleService;

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

}
