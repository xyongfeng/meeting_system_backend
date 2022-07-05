package com.xyongfeng.util;

import com.xyongfeng.pojo.JsonResult;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtil {

    public static String getClassPathUrl() {
        try {
            return ResourceUtils.getURL("classpath:").getPath().substring(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }



    public static boolean uploadFile(MultipartFile file, String subPath, String fileName) {
        try {
            String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
            Path path = Paths.get(classpath).resolve(subPath);
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
}
