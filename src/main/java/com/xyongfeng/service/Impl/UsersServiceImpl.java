package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.UsersMapper;
import com.xyongfeng.pojo.*;
import com.xyongfeng.pojo.Param.UsersAddParam;
import com.xyongfeng.pojo.Param.UsersSetAdminParam;
import com.xyongfeng.pojo.Param.UsersSetImgParam;
import com.xyongfeng.pojo.Param.UsersUpdateParam;
import com.xyongfeng.service.RoleService;
import com.xyongfeng.service.UsersService;
import com.xyongfeng.util.FileUtil;
import com.xyongfeng.util.UserParamConverter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private ImgPathConfig imgPathConfig;

    @Override
    public Users adminLogin(String username, String password) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper
                .eq("username",username)
                .eq("password", password);
        return usersMapper.selectOne(wrapper);
    }

    @Override
    public List<Users> listPage(MyPage myPage) {
        Page<Users> page = new Page<>(myPage.getCurrent(), myPage.getSize());
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
            return users;
        }
        return null;
    }

    @Override
    public Users getUserByUserName(String username) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
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
        return JsonResult.success("修改失败");
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
            return JsonResult.success("上传成功",filename);
        } else {
            return JsonResult.error("上传失败");
        }
    }

    @Override
    public JsonResult setHeadImg(UsersSetImgParam param) {
        JsonResult jsonResult = uploadImg(param.getFile(), imgPathConfig.getHead());
        if(jsonResult.getCode() == 200){
            usersMapper.updateById(new Users().setId(param.getId()).setHeadImage((String)jsonResult.getData()));
            return JsonResult.success(jsonResult.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult setFaceImg(UsersSetImgParam param) {
        return uploadImg(param.getFile(), imgPathConfig.getFace());
    }
}
