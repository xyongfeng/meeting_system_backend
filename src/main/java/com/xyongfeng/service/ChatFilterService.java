package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.ChatFilter;
import com.xyongfeng.pojo.JsonResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
public interface ChatFilterService extends IService<ChatFilter> {
    /**
     * 分页查看过滤词列表
     *
     * @param current
     * @param size
     * @return
     */
    JsonResult select(Integer current, Integer size);

    /**
     * 修改过滤词
     *
     * @param chatFilter
     * @return
     */
    JsonResult updateOne(ChatFilter chatFilter);

    /**
     * 添加过滤词
     *
     * @param chatFilter
     * @return
     */
    JsonResult insert(ChatFilter chatFilter);

    /**
     * 删除过滤词
     *
     * @param id
     * @return
     */
    JsonResult delete(Integer id);

    /**
     * 设置过滤词是否启用
     *
     * @param id
     * @param b
     * @return
     */
    JsonResult setEnable(Integer id, boolean b);

    /**
     * 过滤msg并返回
     *
     * @param msg
     * @return
     */
    String filter(String msg);
}
