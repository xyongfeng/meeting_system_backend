package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.exceptionHandler.exception.NormalException;
import com.xyongfeng.service.ChatFilterService;
import com.xyongfeng.socketer.SockerSender;
import com.xyongfeng.mapper.*;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.*;
import com.xyongfeng.pojo.config.ImgPathPro;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.FileUtil;
import com.xyongfeng.util.JwtTokenUtil;
import com.xyongfeng.util.MyUtil;
import com.xyongfeng.util.UserParamConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
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
    @Autowired
    private UsersFaceFeatureMapper usersFaceFeatureMapper;
    @Autowired
    private ChatFilterService chatFilterService;

    @Value("${flask.headerUrl}")
    private String headerUrl;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public JsonResult select(Integer current, Integer size) {
        IPage<Users> list = listPage(new PageParam(current, size));
        return JsonResult.success(list);
    }

    @Override
    public JsonResult add(UsersAddParam users, PasswordEncoder passwordEncoder) {
        try {
            userAdd(UserParamConverter.getUsers(users), passwordEncoder);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("添加成功", users);
    }

    @Override
    public JsonResult update(UsersUpdateParam users, Integer uid) {
        users.setId(uid);
        Users one = usersMapper.selectOne(new QueryWrapper<Users>().eq("username_xq", users.getUsername()));
        if (one != null && !one.getId().equals(uid)) {
            return JsonResult.error("修改失败，用户名重复");
        }
        one = usersMapper.selectOne(new QueryWrapper<Users>().eq("name_xq", users.getName()));
        if (one != null && !one.getId().equals(uid)) {
            return JsonResult.error("修改失败，名称重复");
        }
        if (usersMapper.updateById(UserParamConverter.getUsers(users)) > 0) {
            return JsonResult.success("修改成功", users);
        } else {
            return JsonResult.error("修改失败");
        }
    }

    @Override
    public JsonResult delete(Integer uid) {

        Users delAdmin = userDelById(uid);
        if (delAdmin != null) {
            return JsonResult.success("删除成功", delAdmin);
        } else {
            return JsonResult.error("删除失败");
        }
    }

    /**
     * 分页获取用户列表
     *
     * @param pageParam current(当前页码),size(页码大小)
     * @return 用户列表
     */
    private IPage<Users> listPage(PageParam pageParam) {
        Page<Users> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        // 输出字段不包括password
        wrapper.select(Users.class, e -> !"password_xq".equals(e.getColumn()));
        wrapper.ne("username_xq", "root");
        usersMapper.selectPage(page, wrapper);
        return page;
    }


    /**
     * 增加新的用户
     *
     * @param users 增加的对象
     * @return 返回执行状态
     */
    private int userAdd(Users users, PasswordEncoder passwordEncoder) throws Exception {

        if (usersMapper.selectOne(new QueryWrapper<Users>().eq("username_xq", users.getUsername())) != null) {
            throw new Exception("用户名重复");
        }
        if (usersMapper.selectOne(new QueryWrapper<Users>().eq("name_xq", users.getName())) != null) {
            throw new Exception("名称重复");
        }

        String encode = passwordEncoder.encode(users.getPassword());
//        log.info(String.valueOf(passwordEncoder.matches(users.getPassword(),encode)));
        // 对密码加密
        users.setPassword(encode);
        // 默认头像
        users.setHeadImage("img\\head\\default.jpg");
        return usersMapper.insert(users);
    }

    /**
     * 根据id删除用户
     *
     * @param id 删除的用户id
     * @return 删除结果
     */
    private Users userDelById(Integer id) {
        Users users = usersMapper.selectById(id);
        if (users != null && usersMapper.deleteById(id) > 0) {
            users.setPassword(null);
            return users;
        }
        return null;
    }

    /**
     * 通过username获取users
     *
     * @param username
     * @return
     */
    @Override
    public Users getUserByUserName(String username) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("username_xq", username));
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

    /**
     * 比较验证码
     *
     * @param request
     * @param receiverCaptcha
     * @return
     */
    public boolean validCaptcha(HttpServletRequest request, String receiverCaptcha) {
        // 判断验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        return captcha != null && captcha.equals(receiverCaptcha);
    }

    /**
     * 通过登录验证之后，保存登录信息
     *
     * @param jwtTokenUtil
     * @param userDetails
     * @return
     */
    private JsonResult saveLogin(JwtTokenUtil jwtTokenUtil, UserDetails userDetails) {
        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return JsonResult.success("登录成功", tokenMap);
    }

    @Override
    public JsonResult login(UsersLoginParam users, HttpServletRequest request, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        if (!validCaptcha(request, users.getCode())) {
            return JsonResult.error("验证码输入错误");
        }
        // 登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(users.getUsername());
        if (null == userDetails || !passwordEncoder.matches(users.getPassword(), userDetails.getPassword())) {
            return JsonResult.error("用户名或密码错误");
        }
        return saveLogin(jwtTokenUtil, userDetails);
    }

    @Override
    public JsonResult loginWithFace(ImgBase64Param param, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        List<UsersFaceFeature> features = usersFaceFeatureMapper.selectAll();
        Map<String, Object> map = new HashMap<>();
        map.put("imgBase64", param.getImgBase64());
        map.put("features", features);

        JSONObject jsonObject = postToFlask(map, "login");
        if (jsonObject.getInteger("code") != 200) {
            return JsonResult.error(jsonObject.getInteger("code"), jsonObject.getString("message"));
        }

        // 得到与该图片差异最小且符合阈值的用户id，这就是登录者
        Integer userId = jsonObject.getInteger("data");
        if (userId == -1) {
            return JsonResult.error("登录失败，没有找到符合账号！");
        }
        Users users = usersMapper.selectById(userId);
        UserDetails userDetails = userDetailsService.loadUserByUsername(users.getUsername());
        return saveLogin(jwtTokenUtil, userDetails);
    }


    @Override
    public JsonResult register(UsersRegisterParam registerUsers, HttpServletRequest request, PasswordEncoder passwordEncoder) {
        if (!validCaptcha(request, registerUsers.getCode())) {
            return JsonResult.error("验证码输入错误");
        }
        Users users = new Users()
                .setUsername(registerUsers.getUsername())
                .setPassword(registerUsers.getPassword())
                .setTelephone(registerUsers.getTelephone())
                .setEmail(registerUsers.getEmail())
                .setName(registerUsers.getName());
        try {
            userAdd(users, passwordEncoder);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.error(e.getMessage());
        }
        return JsonResult.success("注册成功");
    }

    @Override
    public JsonResult getAdminInfo(Principal principal) {
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        Users users = getUserByUserName(username);
        users.setPassword(null);
        return JsonResult.success(users);
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

    private static JsonResult uploadImg(MultipartFile file, String subPath) {

        try {
            // 成功就返回图片相对路径
            String filename = FileUtil.uploadImg(file, subPath);
            if (filename != null) {
                return JsonResult.success("上传成功", Paths.get(subPath).resolve(filename).toString());
            }
        } catch (Exception e) {
            return JsonResult.error("上传失败，".concat(e.getMessage()));
        }
        return JsonResult.error("上传失败");
    }

    @Override
    public JsonResult setHeadImg(UsersSetImgParam param) {
//        param.setId(MyUtil.getUsers().getId());
        JsonResult jsonResult = uploadImg(param.getFile(), imgPathPro.getHead());
        if (jsonResult.getCode() == 200) {
            Users users = new Users().setId(param.getId()).setHeadImage((String) jsonResult.getData());
            usersMapper.updateById(users);
            return JsonResult.success(jsonResult.getMessage(), users);
        }
        return jsonResult;
    }

    private void insertOrUpdateFaceFeature(Integer userId, String faceFeature) {


        if (usersFaceFeatureMapper.update(
                new UsersFaceFeature().setFaceFeature(faceFeature),
                new QueryWrapper<UsersFaceFeature>().eq("user_id_xq", userId)) == 0) {
            usersFaceFeatureMapper.insert(new UsersFaceFeature()
                    .setUserId(userId)
                    .setFaceFeature(faceFeature));
        }
    }

    @Override
    public JsonResult setFaceImg(UsersSetImgParam param) {
        param.setId(MyUtil.getUsers().getId());
        // 设置请求参数map
        Map<String, Object> map = new HashMap<>();
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            map.put("imgBase64", encoder.encode(param.getFile().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送信息给tensorflow进行预测，如果出现问题就返回错误
        JSONObject jsonObject = postToFlask(map, "feature");
        if (jsonObject.getInteger("code") != 200) {
            return JsonResult.error(jsonObject.getString("message"));
        }
        // 上传图片
        JsonResult jsonResult = uploadImg(param.getFile(), imgPathPro.getFace());
        if (jsonResult.getCode() == 200) {
            usersMapper.updateById(new Users().setId(param.getId()).setFaceImage((String) jsonResult.getData()));

            // 修改用户面部照片，如果该用户第一次上传面部照片则插入新数据
            insertOrUpdateFaceFeature(param.getId(), jsonObject.getString("data"));

            return JsonResult.success(jsonResult.getMessage());
        }
        return jsonResult;
    }

    private JSONObject postToFlask(Map<String, Object> requestMap, String subpath) {
        String res = restTemplate.postForObject(headerUrl.concat("/").concat(subpath), requestMap, String.class);
        // 拿到res进行解析
        return JSONObject.parseObject(res);
    }


    @Override
    public JsonResult setFaceImgWithBase64(ImgBase64Param param) {
        param.setUserId(MyUtil.getUsers().getId());

        String filename = FileUtil.uploadImgWithBase64(param.getImgBase64(), imgPathPro.getFace());
        if (filename != null) {
            // 向flask发送base64,flask将此面部的特征信息保存，为人脸识别登录做准备
            // 设置请求参数map
            Map<String, Object> map = new HashMap<>();
            map.put("imgBase64", param.getImgBase64());
            // 发送信息给tensorflow进行预测
            JSONObject jsonObject = postToFlask(map, "feature");
            if (jsonObject.getInteger("code") != 200) {
                return JsonResult.error(jsonObject.getString("message"));
            }
            // 上传
            String s = Paths.get(imgPathPro.getFace()).resolve(filename).toString();
            usersMapper.updateById(new Users().setId(param.getUserId()).setFaceImage(s));
            // 修改用户面部照片，如果该用户第一次上传面部照片则插入新数据
            insertOrUpdateFaceFeature(param.getUserId(), jsonObject.getString("data"));

            // 成功就返回图片相对路径
            return JsonResult.success("上传成功");
        } else {
            return JsonResult.error("上传失败");
        }


    }

    /**
     * 计算sendImgBase64的面部特征，再与用户的特征进行比较，返回的是flask那边的结果
     *
     * @param sendImgBase64
     * @return {code： xxx,message: xxx,userId: xxx}
     */
    private JSONObject compareFace(String sendImgBase64) throws NormalException {
        Users users = MyUtil.getUsers();
        assert users != null;
        if (users.getFaceImage() == null || "".equals(users.getFaceImage())) {
            throw new NormalException("签到需要你的面部照片，请先去个人中心进行上传");
        }

        // 这里直接用数据库保存的特征
        UsersFaceFeature faceFeature = usersFaceFeatureMapper.selectOne(new QueryWrapper<UsersFaceFeature>().eq("user_id_xq", users.getId()));

        String localFeature = faceFeature.getFaceFeature();

        // 设置请求参数map
        Map<String, Object> map = new HashMap<>();
        map.put("sendImgBase64", sendImgBase64);
        map.put("localFeature", localFeature);
        // 发送信息给tensorflow进行预测
        JSONObject jsonObject = postToFlask(map, "predict");
        jsonObject.put("userId", users.getId());
        return jsonObject;
    }

    @Override
    public JsonResult faceVerification(ImgBase64Param param) {
        JSONObject jsonObject = null;
        try {
            jsonObject = compareFace(param.getImgBase64());
        } catch (NormalException e) {
            return JsonResult.error(e.getMessage());
        }
        if (jsonObject.getInteger("code") != 200) {
            return JsonResult.error(jsonObject.getInteger("code"), jsonObject.getString("message"));
        }

        return JsonResult.success("验证成功");
    }

    @Override
    public JsonResult signIn(ImgBase64Param param, String meetingId) {

        if (getHadSignIn(meetingId)) {
            return JsonResult.error("你已经签过到了");
        }
        // 判断签到时间，签到时间必须在会议开始之后
        LocalDateTime startDate = meetingMapper.selectById(meetingId).getStartDate();
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isAfter(now)) {
            return JsonResult.error("会议开始之后才能进行签到");
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = compareFace(param.getImgBase64());
        } catch (Exception e) {
            return JsonResult.error(e.getMessage());
        }

        if (jsonObject.getInteger("code") != 200) {
            return JsonResult.error(jsonObject.getInteger("code"), jsonObject.getString("message"));
        }
        // 已经判断完成了，这里返回了成功的差异值
        String distance = jsonObject.getJSONObject("data").getString("distance");

        QueryWrapper<MeetingUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("meeting_id_xq", meetingId)
                .eq("users_id_xq", jsonObject.getInteger("userId"));
        meetingUsersMapper.update(
                (new MeetingUsers())
                        .setHadSignIn(true)
                        .setHadSignInTime(now), queryWrapper);
        return JsonResult.success("签到成功");
    }

    private Boolean getHadSignIn(String meetingId) {
        QueryWrapper<MeetingUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("meeting_id_xq", meetingId)
                .eq("users_id_xq", MyUtil.getUsers().getId());
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
        return getUserInfo("id_xq", uid.toString());
    }

    @Override
    public JsonResult getUserInfoByName(String userName) {
        return getUserInfo("name_xq", userName);
    }


    @Override
    public JsonResult sendFriApplication(Integer userid) {
        if (usersMapper.selectById(userid) == null) {
            return JsonResult.error("申请失败，该用户不存在");
        }
        Integer ownerId = MyUtil.getUsers().getId();
        if (ownerId.equals(userid)) {
            return JsonResult.error("申请失败，不能对自己发送申请");
        }

        if (usersFriendInformMapper.selectCount(
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type_xq", 0)
                        .eq("from_id_xq", ownerId)
                        .eq("to_id_xq", userid)
                        .eq("state_xq", 0)
        ) > 0) {
            return JsonResult.error("申请失败，你已经对该用户发过申请了");
        }

        if (isFriend(userid, ownerId)) {
            return JsonResult.error("申请失败，你们已经是好友了");
        }
        UsersFriendInform usersFriendInform = new UsersFriendInform()
                .setType(0)
                .setFromId(ownerId)
                .setContent("")
                .setToId(userid)
                .setState(0)
                .setSendTime(LocalDateTime.now());

        usersFriendInformMapper.insert(usersFriendInform);

        usersFriendInform.setFromer(MyUtil.getUsers());

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(usersFriendInform);
        sockerSender.sendInform(jsonObject, 1);
        return JsonResult.success("申请成功");
    }

    private boolean isFriend(Integer userId1, Integer userId2) {
        // 小的id放在前面
        if (userId1 > userId2) {
            swapUserId(userId1, userId2);
        }
        return usersFriendMapper.selectCount(
                (new QueryWrapper<UsersFriend>())
                        .eq("user_id1_xq", userId1)
                        .eq("user_id2_xq", userId2)
                        .or()
                        .eq("user_id2_xq", userId1)
                        .eq("user_id1_xq", userId2)
        ) > 0;
    }

    private static void swapUserId(Integer userId1, Integer userId2) {
        Integer temp = userId1;
        userId1 = userId2;
        userId2 = temp;
    }

    @Override
    public JsonResult replyFriApplication(Integer userid, Integer result) {
        Integer ownerId = MyUtil.getUsers().getId();
        UsersFriendInform usersFriendInform = usersFriendInformMapper.selectOne(
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type_xq", 0)
                        .eq("from_id_xq", userid)
                        .eq("to_id_xq", ownerId)
                        .eq("state_xq", 0)
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
        Integer ownerId = MyUtil.getUsers().getId();
        Page<UsersFriendInform> page = new Page<>(current, size);
        IPage<UsersFriendInform> usersFriendInformPage = usersFriendInformMapper.selectPageWithFromerInfo(page,
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type_xq", 0)
                        .eq("to_id_xq", ownerId)
                        .eq("state_xq", 0)
                        .orderByDesc("send_time_xq")
        );
        return JsonResult.success(usersFriendInformPage);
    }

    @Override
    public JsonResult delFriendById(Integer userid) {
        Integer ownerId = MyUtil.getUsers().getId();

        if (userid > ownerId) {
            swapUserId(userid, ownerId);
        }
        usersFriendInformMapper.delete((new QueryWrapper<UsersFriendInform>())
                .eq("from_id_xq", userid)
                .eq("to_id_xq", ownerId)
                .or()
                .eq("from_id_xq", ownerId)
                .eq("to_id_xq", userid));


        int result = usersFriendMapper.delete(
                (new QueryWrapper<UsersFriend>())
                        .eq("user_id1_xq", userid)
                        .eq("user_id2_xq", ownerId)
                        .or()
                        .eq("user_id1_xq", ownerId)
                        .eq("user_id2_xq", userid)
        );
        if (result == 0) {
            return JsonResult.error("删除失败");
        } else {
            return JsonResult.success("删除成功");
        }
    }

    @Override
    public JsonResult getFriendsAndChat(Integer current, Integer size) {
        Integer ownerId = MyUtil.getUsers().getId();

        Page<Users> page = new Page<>(current, size);

        IPage<Users> usersFriends = usersMapper.selectFriendsAndChatPage(page, ownerId);

        return JsonResult.success(usersFriends);
    }

    @Override
    public JsonResult getFriChat(Integer userid, Integer current, Integer size) {
        Integer ownerId = MyUtil.getUsers().getId();
        Page<UsersFriendInform> page = new Page<>(current, size);
        IPage<UsersFriendInform> usersFriendInformPage = usersFriendInformMapper.selectPage(page,
                (new QueryWrapper<UsersFriendInform>())
                        .eq("type_xq", 1)
                        .and(
                                qw -> qw.eq("from_id_xq", ownerId).eq("to_id_xq", userid)
                                        .or()
                                        .eq("to_id_xq", ownerId).eq("from_id_xq", userid)
                        )
                        .orderByAsc("send_time_xq")
        );
        return JsonResult.success(usersFriendInformPage);
    }


    @Override
    public JsonResult sendFriChat(Integer userid, String conetent) {
        Integer ownerId = MyUtil.getUsers().getId();
        if (!isFriend(userid, ownerId)) {
            return JsonResult.error("发送失败，该好友不存在");
        }
        conetent = chatFilterService.filter(conetent);

        LocalDateTime now = LocalDateTime.now();
        UsersFriendInform usersFriendInform = new UsersFriendInform()
                .setType(1)
                .setFromId(ownerId)
                .setToId(userid)
                .setContent(conetent)
                .setState(0)
                .setSendTime(now);

        usersFriendInformMapper.insert(usersFriendInform);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(usersFriendInform);

        sockerSender.sendChat(jsonObject);
        return JsonResult.success(jsonObject);
    }

    @Override
    public JsonResult readFriChat(Integer userid) {
        usersFriendInformMapper.update(new UsersFriendInform().setState(1),
                new QueryWrapper<UsersFriendInform>()
                        .eq("from_id_xq", userid)
                        .eq("type_xq", 1)
                        .eq("state_xq", 0)
        );
        return JsonResult.success();
    }

    @Override
    public JsonResult updateOwnerInfo(UsersUpdateWithOwnerParam users) {
        Integer uid = MyUtil.getUsers().getId();
        if (!users.getId().equals(uid)) {
            return JsonResult.error("修改失败，无权限修改他人信息");
        }
        Users same_user = usersMapper.selectOne(new QueryWrapper<Users>().eq("name_xq", users.getName()));
        if (same_user != null && !same_user.getId().equals(uid)) {
            return JsonResult.error("修改失败，名称重复");
        }
        Users users1 = new Users()
                .setId(users.getId())
                .setName(users.getName())
                .setEmail(users.getEmail())
                .setTelephone(users.getTelephone());


        int i = usersMapper.updateById(users1);


        if (i > 0) {
            return JsonResult.success("修改成功", users1);
        }
        return JsonResult.error("修改失败");
    }

    @Override
    public JsonResult updateOwnerPass(UsersUpdatePassParam users, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        if (!validCaptcha(request, users.getCode())) {
            return JsonResult.error("验证码输入错误");
        }
        if (!users.getId().equals(MyUtil.getUsers().getId())) {
            return JsonResult.error("修改失败，无权限修改他人信息");
        }
        Users users1 = usersMapper.selectById(users.getId());

        if (passwordEncoder.matches(users.getOldpass(), users1.getPassword())) {
            if (passwordEncoder.matches(users.getNewpass(), users1.getPassword())) {
                return JsonResult.error("修改失败，新密码与当前密码不能重复");
            }
            usersMapper.updateById(new Users()
                    .setId(users.getId())
                    .setPassword(passwordEncoder.encode(users.getNewpass())));
            return JsonResult.success("修改成功");
        }
        return JsonResult.error("修改失败，当前密码错误");
    }


}
