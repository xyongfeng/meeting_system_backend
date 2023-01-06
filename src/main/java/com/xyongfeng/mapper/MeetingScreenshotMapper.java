package com.xyongfeng.mapper;

import com.xyongfeng.pojo.MeetingScreenshot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-31
 */
@Component
public interface MeetingScreenshotMapper extends BaseMapper<MeetingScreenshot> {

    /**
     * 输出该用户所有
     * @return
     */
    List<MeetingScreenshot> selectAll(Integer uid);
}
