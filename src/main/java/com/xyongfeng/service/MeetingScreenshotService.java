package com.xyongfeng.service;

import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.MeetingScreenshot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.Param.ImgBase64Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-31
 */
public interface MeetingScreenshotService extends IService<MeetingScreenshot> {

    /**
     * 接收base64,保存截屏图像
     * @param mid
     * @param param
     * @return
     */
    JsonResult setScreenshotWithBase64(String mid, ImgBase64Param param);

    /**
     * 根据会议id输出该用户的截屏
     * @param mid
     * @return
     */
    JsonResult selectPath(String mid);

    /**
     * 输出该用户所有会议截屏
     * @return
     */
    JsonResult selectAll();

    /**
     * 根据id删除
     * @param id
     * @return
     */
    JsonResult deleteById(String id);
}
