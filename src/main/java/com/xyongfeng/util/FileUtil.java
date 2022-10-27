package com.xyongfeng.util;

import com.xyongfeng.pojo.JsonResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class FileUtil {

    public static Path getStaticResPathUrl() {
        try {
            String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
            return Paths.get(classpath).resolve("static");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean uploadFile(MultipartFile file, String subPath, String fileName) {
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


    public static boolean uploadFile(byte[] bytes, String subPath, String fileName) {
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

    public static String uploadImg(MultipartFile file, String subPath) throws Exception {
        try {
            if (file.getBytes().length / 1024 / 1024 > 2) {
                throw new Exception("文件大小不能超过2M");
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

    public static String getImgWithBase64(String subpath) {
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
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }
}
