package com.xyongfeng.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author xyongfeng
 */
@Component
@ConfigurationProperties(prefix = "img")
@Data
public class ImgPathConfig {
    private String head;
    private String face;
}
