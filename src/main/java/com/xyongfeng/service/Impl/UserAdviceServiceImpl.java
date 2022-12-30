package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.UserAdviceMapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.UserAdvice;
import com.xyongfeng.service.UserAdviceService;
import com.xyongfeng.util.MyUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyongfeng
 * @since 2022-12-29
 */
@Service
public class UserAdviceServiceImpl extends ServiceImpl<UserAdviceMapper, UserAdvice> implements UserAdviceService {
    @Autowired
    private UserAdviceMapper userAdviceMapper;

    @Override
    public JsonResult select(Integer current, Integer size) {
        return JsonResult.success(userAdviceMapper.selectWithUser(new Page<>(current, size)));
    }

    @Override
    public JsonResult insert(UserAdvice userAdvice) {

        userAdvice.setUserId(MyUtil.getUsers().getId());
        userAdvice.setTime(LocalDateTime.now());
        int i = userAdviceMapper.insert(userAdvice);

        if (i > 0) {
            return JsonResult.success("感谢您的反馈");
        }
        return JsonResult.error("反馈失败");

    }

    @Override
    public JsonResult delete(Integer id) {

        int i = userAdviceMapper.deleteById(id);
        if (i > 0) {
            return JsonResult.success("感谢您的反馈");
        }
        return JsonResult.error("反馈失败");
    }
}
