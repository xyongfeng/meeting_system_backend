package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.AdminLog;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.Param.AdminLogSearchParam;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
public interface AdminLogService extends IService<AdminLog> {

    void insert(String actionModule, String actionType, String actionUrl, String actionContent,Boolean actionSuccess);

    JsonResult select(Integer current, Integer size, Map<String, String> params);

    void insert(String actionModule, String actionType, String actionUrl,  Boolean actionSuccess);
}
