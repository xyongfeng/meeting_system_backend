package com.xyongfeng.util;

import com.xyongfeng.MeetingApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {


    @Autowired
    private Environment environment;

    public Path getStaticResPathUrl() {
        try {
            // 获取classpath
            String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
            // 获取运行环境
            String[] activeProfiles = environment.getActiveProfiles();
            // 如果运行环境是prod，则代表是jar运行方式，则classpath为jar运行的根目录
            if (activeProfiles.length > 0 && "prod".equals(activeProfiles[0])) {
                ApplicationHome home = new ApplicationHome(MeetingApplication.class);

                classpath = home.getSource().getParentFile().toString();
            }
//            String path = System.getProperty("user.dir")+"/static/images/upload/";
            log.info(String.format("classpath: %s", classpath));
            return Paths.get(classpath).resolve("static");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean uploadFile(MultipartFile file, String subPath, String fileName) {
        try {
            Path path = Objects.requireNonNull(getStaticResPathUrl()).resolve(subPath);
            // 创建文件夹
            Files.createDirectories(path);
            // 写入文件
            Files.copy(file.getInputStream(), path.resolve(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean uploadFile(byte[] bytes, String subPath, String fileName) {
        try {
            Path path = Objects.requireNonNull(getStaticResPathUrl()).resolve(subPath);
            // 创建文件夹
            Files.createDirectories(path);
            // 写入文件
            OutputStream out = new FileOutputStream(path.resolve(fileName).toString());
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String uploadImg(MultipartFile file, String subPath) throws Exception {
        try {
            if (file.getBytes().length / 1024 / 1024 > 5) {
                throw new Exception("文件大小不能超过5M");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String end = ".jpg";
        if (originalFilename.endsWith(".jpg")) {
            end = ".jpg";
        } else if (originalFilename.endsWith(".jpeg")) {
            end = ".jpeg";
        } else if (originalFilename.endsWith(".png")) {
            end = ".png";
        } else {
            throw new Exception("文件格式只能是jpeg与png");
        }
        String filename = UUID.randomUUID().toString().concat(end);
        return uploadFile(file, subPath, filename) ? filename : null;
    }

    /**
     * 将本地图片变成Base64形式
     *
     * @param subpath
     * @return
     */
    public String getImgWithBase64(String subpath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            //获取图片路径
            String path = Objects.requireNonNull(getStaticResPathUrl()).resolve(subpath).toString();
            in = new FileInputStream(path);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        // 返回Base64编码过的字节数组字符串
        return new String(encoder.encode(data));
    }

    /**
     * 上传Base64格式的图片
     *
     * @return
     */
    public String uploadImgWithBase64(String imgBase64, String savePath) {
        Base64.Decoder decoder = Base64.getDecoder();
        // Base64解码
        byte[] bytes = decoder.decode(imgBase64);
        // 调整异常数据
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        String filename = UUID.randomUUID().toString().concat(".jpg");
        return uploadFile(bytes, savePath, filename) ? filename : null;
    }
}
