package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.MeetingScreenshot;
import com.xyongfeng.mapper.MeetingScreenshotMapper;
import com.xyongfeng.pojo.Param.ImgBase64Param;
import com.xyongfeng.pojo.config.ImgPathPro;
import com.xyongfeng.service.MeetingScreenshotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.util.FileUtil;
import com.xyongfeng.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-31
 */
@Service
public class MeetingScreenshotServiceImpl extends ServiceImpl<MeetingScreenshotMapper, MeetingScreenshot> implements MeetingScreenshotService {
    @Autowired
    private MeetingScreenshotMapper meetingScreenshotMapper;
    @Autowired
    private ImgPathPro imgPathPro;


    private int insert(String mid, Integer uid, String filename) {
        return meetingScreenshotMapper.insert(new MeetingScreenshot().setUserId(uid).setMeetingId(mid).setPath(imgPathPro.getScreenshot().concat("/").concat(filename)));
    }

    @Override
    public JsonResult setScreenshotWithBase64(String mid, ImgBase64Param param) {
        String filename = FileUtil.uploadImgWithBase64(param.getImgBase64(), imgPathPro.getScreenshot());
        if (filename != null) {
            int insert = insert(mid, MyUtil.getUsers().getId(), filename);
            if (insert > 0) {
                return JsonResult.success();
            }
        }
        return JsonResult.error("保存失败");
    }

    @Override
    public JsonResult selectPath(String mid) {
        List<Object> objects = meetingScreenshotMapper.selectObjs(new QueryWrapper<MeetingScreenshot>().select("path_xq").eq("meeting_id_xq", mid).eq("user_id_xq", MyUtil.getUsers().getId()));
        return JsonResult.success(objects);
    }

    @Override
    public JsonResult selectAll() {
        return JsonResult.success(meetingScreenshotMapper.selectAll(MyUtil.getUsers().getId()));
    }

    @Override
    public JsonResult deleteById(String id) {
        int i = meetingScreenshotMapper.deleteById(id);
        if (i > 0) {
            return JsonResult.success("删除成功");
        }else{
            return JsonResult.error("删除失败");
        }
    }
}
