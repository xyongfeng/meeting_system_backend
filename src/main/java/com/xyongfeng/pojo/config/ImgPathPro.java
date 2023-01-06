package com.xyongfeng.pojo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author xyongfeng
 */
@Component
@ConfigurationProperties(prefix = "img")
@Data
public class ImgPathPro {
    private String head;
    private String face;
    private String screenshot;
    private String adviceImg;
}
