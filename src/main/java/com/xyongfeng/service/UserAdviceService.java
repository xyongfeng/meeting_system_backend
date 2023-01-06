package com.xyongfeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.UserAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
public interface UserAdviceService extends IService<UserAdvice> {

    JsonResult select(Integer current, Integer size);

    JsonResult insert(UserAdvice userAdvice, List<MultipartFile> files);

    JsonResult delete(Integer id);
}
