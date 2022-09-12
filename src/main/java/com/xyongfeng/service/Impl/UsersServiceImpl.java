package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.Socketer.SockerSender;
import com.xyongfeng.Socketer.SocketHandler;
import com.xyongfeng.mapper.*;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.pojo.config.ImgPathPro;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.FileUtil;
import com.xyongfeng.util.SecurityUtil;
import com.xyongfeng.util.UserParamConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xyongfeng
 */
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ImgPathPro imgPathPro;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MeetingUsersMapper meetingUsersMapper;
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private UsersFriendInformMapper usersFriendInformMapper;
    @Autowired
    private UsersFriendMapper usersFriendMapper;
    @Autowired
    private SockerSender sockerSender;

    @Value("${flask.headerUrl}")
    private String headerUrl;

    @Override
    public Users adminLogin(String username, String password) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper
                .eq("username", username)
                .eq("password", password);
        return usersMapper.selectOne(wrapper);
    }

    @Override
    public IPage<Users> listPage(PageParam pageParam) {
        Page<Users> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        // 输出字段不包括password
        wrapper.select(Users.class, e -> !"password".equals(e.getColumn()));
        usersMapper.selectPage(page, wrapper);
        return page;
    }


    @Override
    public int userUpdateById(UsersUpdateParam usersUpdateParam) {

        return usersMapper.updateById(UserParamConverter.getUsers(usersUpdateParam));
    }


    @Override
    public int userAdd(UsersAddParam users, PasswordEncoder passwordEncoder) throws Exception {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username", users.getUsername());
        List<Map<String, Object>> list = usersMapper.selectMaps(wrapper);
        if (list.size() > 0) {
            throw new Exception("用户名重复");
        }
        // 对密码加密
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        // 默认头像
        users.setHeadImage("img\\head\\default.jpg");
        return usersMapper.insert(UserParamConverter.getUsers(users));
    }

    @Override
    public Users userDelById(Integer id) {
        Users users = usersMapper.selectById(id);
        if (users != null && usersMapper.deleteById(id) > 0) {
            users.setPassword(null);
            return users;
        }
        return null;
    }

    @Override
    public Users getUserByUserName(String username) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
        if (users == null) {
            return null;
        }
        List<Role> roles = roleService.selectRoleWithUserid(users.getId());
        List<String> perms = roles.stream().map(Role::getPerms).collect(Collectors.toList());
        if (users.getIsAdmin()) {
            perms.add("admin");
        }
        users.setPerms(perms);
        return users;
    }

    @Override
    public JsonResult setAdmin(UsersSetAdminParam param) {
        int i = usersMapper.updateById(
                new Users()
                        .setId(param.getId())
                        .setIsAdmin(param.getIsAdmin()));
        if (i != 0) {
            return JsonResult.success("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    public static JsonResult uploadImg(MultipartFile file, String subPath) {
        try {
            if (file.getBytes().length / 1024 / 1024 > 2) {
                return JsonResult.error("文件大小不能超过2M");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String end = ".jpg";
        if (originalFilename.endsWith(".jpg")) {
            end = ".jpg";
        } else if (originalFilename.endsWith(".jpeg")) {
            end = ".jpeg";
        } else if (originalFilename.endsWith(".png")) {
            end = ".png";
        } else {
            return JsonResult.error("文件格式只能是jpeg与png");
        }
        String filename = UUID.randomUUID().toString().concat(end);
        if (FileUtil.uploadFile(file, subPath, filename)) {
            // 成功就返回图片相对路径
            return JsonResult.success("上传成功", Paths.get(subPath).resolve(filename).toString());
        } else {
            return JsonResult.error("上传失败");
        }
    }

    @Override
    public JsonResult setHeadImg(UsersSetImgParam param) {
        JsonResult jsonResult = uploadImg(param.getFile(), imgPathPro.getHead());
        if (jsonResult.getCode() == 200) {
            usersMapper.updateById(new Users().setId(param.getId()).setHeadImage((String) jsonResult.getData()));
            return JsonResult.success(jsonResult.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult setFaceImg(UsersSetImgParam param) {
        return uploadImg(param.getFile(), imgPathPro.getFace());
    }

    @Override
    public JsonResult signIn(UsersSignInParam param, String meetingId) {

        if (getHadSignIn(meetingId)) {
            return JsonResult.error("你已经签过到了");
        }
        // todo 如果会议结束了，也不能进行签到
        // 判断签到时间，签到时间必须在会议开始之后
        LocalDateTime startDate = meetingMapper.selectById(meetingId).getStartDate();
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isAfter(now)) {
            return JsonResult.error("会议开始之后才能进行签到");
        }
        String imgBase64 = param.getImgBase64();

        Map<String, Object> map = new HashMap<>();
        map.put("imgBase64", imgBase64);

        String res = restTemplate.postForObject(headerUrl.concat("/predict"), map, String.class);

        JSONObject jsonObject = JSONObject.parseObject(res);

        if (jsonObject.getInteger("code") != 200) {
            return JsonResult.error(jsonObject.getInteger("code"), jsonObject.getString("message"));
        }

        String name = jsonObject.getJSONObject("data").getString("name");

        if (!name.equals(Objects.requireNonNull(SecurityUtil.getUsers()).getName())) {
            return JsonResult.error("签到失败，人脸检测非本人");
        }

        QueryWrapper<MeetingUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("meeting_id", meetingId)
                .eq("users_id", SecurityUtil.getUsers().getId());
        meetingUsersMapper.update(
                (new MeetingUsers())
                        .setHadSignIn(true)
                        .setHadSignInTime(now), queryWrapper);
        return JsonResult.success("签到成功");
    }

    private Boolean getHadSignIn(String meetingId) {
        QueryWrapper<MeetingUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("meeting_id", meetingId)
                .eq("users_id", SecurityUtil.getUsers().getId());
        MeetingUsers meetingUsers = meetingUsersMapper.selectOne(queryWrapper);
        if (meetingUsers == null) {
            return false;
        }
        return meetingUsers.getHadSignIn();
    }


    @Override
    public JsonResult hadSignIn(String meetingId) {

        return JsonResult.success(getHadSignIn(meetingId));
    }

    private JsonResult getUserInfo(String key, String value) {
        Users users = usersMapper.selectOne(
                (new QueryWrapper<Users>())
                        .eq(key, value));
        if (users == null) {
            return JsonResult.error("没有找到该用户信息");
        }
        users
                .setPassword(null)
                .setIsAdmin(null);

        return JsonResult.success(users);
    }

    @Override
    public JsonResult getUserInfoById(Integer uid) {
        return getUserInfo("id", uid.toString());
    }

    @Override
    public JsonResult getUserInfoByName(String userName) {
        return getUserInfo("name", userName);
    }


    @Override
    public JsonResult sendFriApplication(Integer userid) {
        if (usersMapper.selectById(userid) == null) {
            return JsonResult.error("申请失败，该用户不存在");
        }
        Integer ownerId = SecurityUtil.getUsers().getId();
        if (ownerId.equals(userid)) {
            return JsonResult.error("申请失败，不能对自己发送申请");
        }

        if (usersFriendInformMapper.selectCount(
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type", 0)
                        .eq("from_id", ownerId)
                        .eq("to_id", userid)
                        .eq("state", 0)
        ) > 0) {
            return JsonResult.error("申请失败，你已经对该用户发过申请了");
        }

        if (isFriend(userid, ownerId)) {
            return JsonResult.error("申请失败，你们已经是好友了");
        }

        usersFriendInformMapper.insert((new UsersFriendInform())
                .setType(0)
                .setFromId(ownerId)
                .setContent("")
                .setToId(userid)
                .setState(0)
                .setSendTime(LocalDateTime.now())
        );
        sockerSender.addInform(userid, 1);
        return JsonResult.success("申请成功");
    }

    private boolean isFriend(Integer userId1, Integer userId2) {
        // 小的id放在前面
        if (userId1 > userId2) {
            swapUserId(userId1, userId2);
        }
        return usersFriendMapper.selectCount(
                (new QueryWrapper<UsersFriend>())
                        .eq("user_id1", userId1)
                        .eq("user_id2", userId2)
        ) > 0;
    }

    private static void swapUserId(Integer userId1, Integer userId2) {
        Integer temp = userId1;
        userId1 = userId2;
        userId2 = temp;
    }

    @Override
    public JsonResult replyFriApplication(Integer userid, Integer result) {
        Integer ownerId = SecurityUtil.getUsers().getId();
        UsersFriendInform usersFriendInform = usersFriendInformMapper.selectOne(
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type", 0)
                        .eq("from_id", userid)
                        .eq("to_id", ownerId)
                        .eq("state", 0)
        );
        if (usersFriendInform == null) {
            return JsonResult.error("处理失败，找不到该处理对象");
        }
        // 已读
        usersFriendInform.setState(1);
        usersFriendInformMapper.updateById(usersFriendInform);

        if (result == 1) {
            // 同意
            if (userid > ownerId) {
                swapUserId(userid, ownerId);
            }

            usersFriendMapper.insert((new UsersFriend())
                    .setUserId1(userid)
                    .setUserId2(ownerId));
            sendFriChat(userid, "我同意了你的好友请求,现在可以进行聊天了");
            return JsonResult.success("同意成功");
        } else {
            // 忽略
            return JsonResult.success("忽略成功");
        }


    }

    @Override
    public JsonResult getFriApplications(Integer current, Integer size) {
        Integer ownerId = SecurityUtil.getUsers().getId();
        Page<UsersFriendInform> page = new Page<>(current, size);
        IPage<UsersFriendInform> usersFriendInformPage = usersFriendInformMapper.selectPageWithFromerInfo(page,
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type", 0)
                        .eq("to_id", ownerId)
                        .eq("state", 0)
                        .orderByDesc("send_time")
        );
        return JsonResult.success(usersFriendInformPage);
    }

    @Override
    public JsonResult delFriendById(Integer userid) {
        Integer ownerId = SecurityUtil.getUsers().getId();

        if (userid > ownerId) {
            swapUserId(userid, ownerId);
        }

        int result = usersFriendMapper.delete(
                (new QueryWrapper<UsersFriend>())
                        .eq("user_id1", userid)
                        .eq("user_id2", ownerId)
        );
        if (result == 0) {
            return JsonResult.error("删除失败");
        } else {
            return JsonResult.success("删除成功");
        }
    }

    @Override
    public JsonResult getFriendsAndChat(Integer current, Integer size) {
        Integer ownerId = SecurityUtil.getUsers().getId();

        Page<Users> page = new Page<>(current, size);

        IPage<Users> usersFriends = usersMapper.selectFriendsAndChatPage(page, ownerId);

        return JsonResult.success(usersFriends);
    }

    @Override
    public JsonResult getFriChat(Integer userid, Integer current, Integer size) {
        Integer ownerId = SecurityUtil.getUsers().getId();
        Page<UsersFriendInform> page = new Page<>(current, size);
        IPage<UsersFriendInform> usersFriendInformPage = usersFriendInformMapper.selectPage(page,
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type", 1)
                        .and(
                                qw -> qw.eq("from_id", ownerId).eq("to_id", userid)
                                        .or()
                                        .eq("to_id", ownerId).eq("from_id", userid)
                        )
                        .orderByAsc("send_time")
        );
        return JsonResult.success(usersFriendInformPage);
    }


    @Override
    public JsonResult sendFriChat(Integer userid, String conetent) {
        Integer ownerId = SecurityUtil.getUsers().getId();
        LocalDateTime now = LocalDateTime.now();
        usersFriendInformMapper.insert(
                (new UsersFriendInform())
                        .setType(1)
                        .setFromId(ownerId)
                        .setToId(userid)
                        .setContent(conetent)
                        .setState(0)
                        .setSendTime(now)
        );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("toId", userid);
        jsonObject.put("fromId", ownerId);
        jsonObject.put("content", conetent);
        jsonObject.put("sendTime", now.toString());
        sockerSender.sendChat(jsonObject);
        return JsonResult.success(jsonObject);
    }

    @Override
    public JsonResult readFriChat(Integer userid) {
        usersFriendInformMapper.update(new UsersFriendInform().setState(1),
                new QueryWrapper<UsersFriendInform>()
                        .eq("from_id", userid)
                        .eq("type", 1)
                        .eq("state", 0)
        );
        return JsonResult.success();
    }
}
