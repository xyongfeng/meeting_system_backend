package com.xyongfeng.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.AdminLogMapper;
import com.xyongfeng.pojo.AdminLog;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.service.AdminLogService;
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
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {
    @Autowired
    private AdminLogMapper adminLogMapper;

    @Override
    public void insert(String actionModule, String actionType, String actionUrl, String actionContent, Boolean actionSuccess) {
        AdminLog adminLog = new AdminLog()
                .setActionTime(LocalDateTime.now())
                .setActionUserId(MyUtil.getUsers().getId())
                .setActionContent(actionContent)
                .setActionType(actionType)
                .setActionUrl(actionUrl)
                .setActionModule(actionModule)
                .setActionSuccess(actionSuccess);

        adminLogMapper.insert(adminLog);
    }

    @Override
    public void insert(String actionModule, String actionType, String actionUrl, Boolean actionSuccess) {
        insert(actionModule, actionType, actionUrl, "", actionSuccess);
    }

    @Override
    public JsonResult select(Integer current, Integer size) {

        return JsonResult.success(adminLogMapper.selectWithUser(new Page<>(current, size)));
    }
}
