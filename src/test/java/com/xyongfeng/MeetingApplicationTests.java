package com.xyongfeng;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyongfeng.mapper.MeetingApplicationMapper;
import com.xyongfeng.mapper.MenuMapper;
import com.xyongfeng.mapper.UsersMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.MeetingApplication;
import com.xyongfeng.pojo.Param.PageParam;
import com.xyongfeng.service.MeetingService;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.MyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@SpringBootTest
class MeetingApplicationTests {


}
