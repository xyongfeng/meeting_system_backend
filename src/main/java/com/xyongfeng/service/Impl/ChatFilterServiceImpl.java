package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.ChatFilterMapper;
import com.xyongfeng.pojo.ChatFilter;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.ChatFilterService;
import com.xyongfeng.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Service
public class ChatFilterServiceImpl extends ServiceImpl<ChatFilterMapper, ChatFilter> implements ChatFilterService {
    @Autowired
    private ChatFilterMapper chatFilterMapper;

    @Override
    public JsonResult select(Integer current, Integer size) {

        return JsonResult.success(chatFilterMapper.selectWithUser(new Page<>(current, size)));
    }

    @Override
    public JsonResult updateOne(ChatFilter chatFilter) {

        int i = chatFilterMapper.updateById(chatFilter);
        if (i > 0) {
            return JsonResult.success("修改成功", chatFilter);
        }
        return JsonResult.error("修改失败");
    }

    @Override
    public JsonResult insert(ChatFilter chatFilter) {
        chatFilter.setAppenderId(MyUtil.getUsers().getId());
        chatFilter.setAppendTime(LocalDateTime.now());
        int i = chatFilterMapper.insert(chatFilter);
        if (i > 0) {
            return JsonResult.success("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    @Override
    public JsonResult delete(Integer id) {

        int i = chatFilterMapper.deleteById(id);
        if (i > 0) {
            return JsonResult.success("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    @Override
    public JsonResult setEnable(Integer id, boolean b) {

        int i = chatFilterMapper.updateById(new ChatFilter().setId(id).setEnable(b));

        if (i > 0) {
            return JsonResult.success("修改成功");
        }
        return JsonResult.error("修改失败");
    }

    @Override
    public String filter(String msg) {
        List<ChatFilter> filterItems = chatFilterMapper.selectList(new QueryWrapper<ChatFilter>().eq("enable_xq", 1));
        for (ChatFilter filterItem : filterItems) {
            if (filterItem.getFilterRule() == 1) {
                msg = msg.replace(filterItem.getFilterContent(), filterItem.getReplaceContent());
            } else {
                msg = msg.replaceAll(filterItem.getFilterContent(), filterItem.getReplaceContent());
            }
        }
        return msg;
    }
}
