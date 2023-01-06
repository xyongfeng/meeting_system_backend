package com.xyongfeng.service;

import com.xyongfeng.pojo.MeetingPassword;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2023-01-03
 */
public interface MeetingPasswordService extends IService<MeetingPassword> {

    int insert(String id, String password, Boolean passEnabled);

    /**
     * 返回会议密码，如果没有就返回空
     *
     * @param mid
     * @return
     */
    MeetingPassword getMeetingPasswordOne(String mid);
}
