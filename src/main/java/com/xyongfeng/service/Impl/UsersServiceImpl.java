package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.UsersMapper;
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

    @Value("${flask.headerUrl}")
    private String headerUrl;

    @Override
    public Users adminLogin(String username, String password) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper
                .eq("username",username)
                .eq("password", password);
        return usersMapper.selectOne(wrapper);
    }

    @Override
    public List<Users> listPage(PageParam pageParam) {
        Page<Users> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        // 输出字段不包括password
        wrapper.select(Users.class, e -> !"password".equals(e.getColumn()));
        usersMapper.selectPage(page, wrapper);
        return page.getRecords();
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
        if(users == null){
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
            return JsonResult.success("上传成功",Paths.get(subPath).resolve(filename).toString());
        } else {
            return JsonResult.error("上传失败");
        }
    }

    @Override
    public JsonResult setHeadImg(UsersSetImgParam param) {
        JsonResult jsonResult = uploadImg(param.getFile(), imgPathPro.getHead());
        if(jsonResult.getCode() == 200){
            usersMapper.updateById(new Users().setId(param.getId()).setHeadImage((String)jsonResult.getData()));
            return JsonResult.success(jsonResult.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult setFaceImg(UsersSetImgParam param) {
        return uploadImg(param.getFile(), imgPathPro.getFace());
    }

    @Override
    public JsonResult register(UsersRegisterParam param) {
        String imgBase64 = param.getImgBase64();

        Map<String,Object> map = new HashMap<>();
        map.put("imgBase64",imgBase64);

        String res = restTemplate.postForObject(headerUrl.concat("/predict"), map, String.class);

        JSONObject jsonObject = JSONObject.parseObject(res);

        if(jsonObject.getInteger("code") != 200){
            return JsonResult.error(jsonObject.getInteger("code"),jsonObject.getString("message"));
        }

        String name = jsonObject.getJSONObject("data").getString("name");

        if (!name.equals(Objects.requireNonNull(SecurityUtil.getUsers()).getName())){
            return JsonResult.error("签到失败，人脸检测非本人");
        }

        return JsonResult.success("签到成功");
    }
}
