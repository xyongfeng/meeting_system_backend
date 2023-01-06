package com.xyongfeng.service;

import com.xyongfeng.pojo.UsersFriendInform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-08-22
 */
public interface UsersFriendInformService extends IService<UsersFriendInform> {

    List<Object> getAllContent();
}
