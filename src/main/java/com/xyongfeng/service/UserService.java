package com.xyongfeng.service;

import com.xyongfeng.mapper.UserMapper;
import com.xyongfeng.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-06-09
 */
public interface UserService extends IService<User> {
    /**
     * 返回用户列表
     * @return 用户列表
     */
    List<User> getAll();
    /**
     * 加入用户
     * @param user 加入的用户
     * @return 用户
     */
    User insert(User user);
    /**
     * 删除用户
     * @return 是否成功
     */
    boolean deleteById();
}
