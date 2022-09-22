package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.Meeting;
import com.xyongfeng.pojo.MeetingUsers;
import com.xyongfeng.pojo.Users;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-07-20
 */
@Component
public interface MeetingUsersMapper extends BaseMapper<MeetingUsers> {

    IPage<Users> selectHadSignInList(Page<Users> page,String mid );
}
