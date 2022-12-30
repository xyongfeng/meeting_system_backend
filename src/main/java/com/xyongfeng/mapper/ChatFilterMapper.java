package com.xyongfeng.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyongfeng.pojo.ChatFilter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xyongfeng.pojo.Meeting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Component
public interface ChatFilterMapper extends BaseMapper<ChatFilter> {
    IPage<ChatFilter> selectWithUser(Page<ChatFilter> page);

    ChatFilter selectWithUserById(@Param("userId") Integer userId);


}
