package com.xyongfeng.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.xyongfeng.mapper.MeetingChatMapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.MeetingChat;
import com.xyongfeng.pojo.UsersFriendInform;
import com.xyongfeng.service.MeetingChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.service.UsersFriendInformService;
import com.xyongfeng.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-30
 */
@Service
public class MeetingChatServiceImpl extends ServiceImpl<MeetingChatMapper, MeetingChat> implements MeetingChatService {
    @Autowired
    private MeetingChatMapper meetingChatMapper;
    @Autowired
    private UsersFriendInformService usersFriendInformService;
    @Autowired
    private FileUtil fileUtil;

    @Override
    public void insert(String meetingId, Integer userId, String msg) {
        meetingChatMapper.insert(new MeetingChat()
                .setMeetingId(meetingId).setUserId(userId).setMsg(msg).setSendTime(LocalDateTime.now()));
    }

    @Override
    public JsonResult selectChatJieba() {

        // 获取停用词文件
        Path stopwordPath = Objects.requireNonNull(fileUtil.getStaticResPathUrl()).resolve("stopwords.txt");
        List<String> stopwords = null;
        try {
            // 获取每一行
            stopwords = Files.readAllLines(stopwordPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取jieba分词对象
        JiebaSegmenter segmenter = new JiebaSegmenter();
        // 获取所有好友消息
        List<Object> allContent = usersFriendInformService.getAllContent();
        // 获取所有会议消息
        List<Object> msg = meetingChatMapper.selectObjs(new QueryWrapper<MeetingChat>().select("msg_xq"));
        allContent.addAll(msg);
        Map<String, Integer> map = new HashMap<>();

        for (Object c : allContent) {
            // 将中文以外的字符去掉
            String s = c.toString().replaceAll("[^\\u4e00-\\u9fa5]", "");
            for (String ss : segmenter.sentenceProcess(s)) {
                // 属于停用词的不要
                if (filterStopword(stopwords, ss)) {
                    continue;
                }
                if (map.containsKey(ss)) {
                    map.put(ss, map.get(ss) + 1);
                } else {
                    map.put(ss, 1);
                }
            }
        }

        return JsonResult.success(map);
    }

    /**
     * 如果是停用词则返回真
     *
     * @param stopwords
     * @param ss
     * @return
     */
    private boolean filterStopword(List<String> stopwords, String ss) {
        if (stopwords == null) {
            return false;
        }
        return stopwords.contains(ss);
    }

    @Override
    public JsonResult selectStopword() {
        Path stopwordPath = Objects.requireNonNull(fileUtil.getStaticResPathUrl()).resolve("stopwords.txt");
        List<String> strings = null;
        try {
            strings = Files.readAllLines(stopwordPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JsonResult.success(strings);
    }

    @Override
    public JsonResult updateStopword(List<String> words) {
        Path stopwordPath = Objects.requireNonNull(fileUtil.getStaticResPathUrl()).resolve("stopwords.txt");

        byte[] bytes = String.join("\n", words).getBytes();
        try {
            OutputStream out = new FileOutputStream(stopwordPath.toString());

            out.write(bytes);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JsonResult.success("保存成功");
    }
}
