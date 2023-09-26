package com.xyongfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyongfeng.mapper.UserAdviceImgMapper;
import com.xyongfeng.mapper.UserAdviceMapper;
import com.xyongfeng.pojo.JsonResult;
import com.xyongfeng.pojo.UserAdvice;
import com.xyongfeng.pojo.UserAdviceImg;
import com.xyongfeng.pojo.config.ImgPathPro;
import com.xyongfeng.service.UserAdviceService;
import com.xyongfeng.util.FileUtil;
import com.xyongfeng.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class UserAdviceServiceImpl extends ServiceImpl<UserAdviceMapper, UserAdvice> implements UserAdviceService {
    @Autowired
    private UserAdviceMapper userAdviceMapper;
    @Autowired
    private UserAdviceImgMapper userAdviceImgMapper;

    @Autowired
    private ImgPathPro imgPathPro;

    @Autowired
    private FileUtil fileUtil;

    private List<String> getAdviceImgs(UserAdvice userAdvice) {
        List<Object> objects = userAdviceImgMapper.selectObjs(new QueryWrapper<UserAdviceImg>()
                .eq("advice_id_xq", userAdvice.getId()).select("img_path_xq"));
        List<String> strings = new ArrayList<>();
        objects.forEach(x -> strings.add(x.toString()));

        return strings.size() > 0 ? strings : null;
    }

    @Override
    public JsonResult select(Integer current, Integer size) {
        IPage<UserAdvice> userAdviceIPage = userAdviceMapper.selectWithUserAndImg(new Page<>(current, size));

        userAdviceIPage.getRecords().forEach(x -> x.setImgs(getAdviceImgs(x)));

        return JsonResult.success(userAdviceIPage);
    }

    @Override
    public JsonResult insert(UserAdvice userAdvice, List<MultipartFile> files) {

        userAdvice.setUserId(MyUtil.getUsers().getId());
        userAdvice.setTime(LocalDateTime.now());
        int i = userAdviceMapper.insert(userAdvice);

        for (MultipartFile file : files) {
            try {
                // 判断文件大小是否为0，则表示该文件为占用空文件，不能上传
                if(file.getSize() == 0){
                    continue;
                }
                // 上传图片
                String filename = fileUtil.uploadImg(file, imgPathPro.getAdviceImg());

                assert filename != null;
                String filePath = Paths.get(imgPathPro.getAdviceImg()).resolve(filename).toString();
                // 加入数据库
                userAdviceImgMapper.insert(new UserAdviceImg().setAdviceId(userAdvice.getId()).setImgPath(filePath));
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.error("反馈失败 ".concat(e.getMessage()));
            }
        }

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
