package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyongfeng.pojo.UsersFriendInform;
import com.xyongfeng.mapper.UsersFriendInformMapper;
import com.xyongfeng.service.UsersFriendInformService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-08-22
 */
@Service
public class UsersFriendInformServiceImpl extends ServiceImpl<UsersFriendInformMapper, UsersFriendInform> implements UsersFriendInformService {
    @Autowired
    private UsersFriendInformMapper usersFriendInformMapper;

    @Override
    public List<Object> getAllContent() {
        return usersFriendInformMapper.selectObjs(new QueryWrapper<UsersFriendInform>()
                .select("content_xq").eq("type_xq", 1));
    }
}
