package com.xyongfeng.service;

import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.MeetingChat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-30
 */
public interface MeetingChatService extends IService<MeetingChat> {
    /**
     * 插入聊天内容
     * @param meetingId
     * @param userId
     * @param msg
     */
    void insert(String meetingId, Integer userId, String msg);

    /**
     * 查看所有聊天内容的分词结果
     * @return
     */
    JsonResult selectChatJieba();

    /**
     * 查看分词的停用词
     * @return
     */
    JsonResult selectStopword();

    /**
     * 修改分词的停用词
     * @return
     */
    JsonResult updateStopword(List<String> words);
}
