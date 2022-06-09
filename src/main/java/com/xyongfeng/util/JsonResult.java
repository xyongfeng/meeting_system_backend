package com.xyongfeng.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@ApiModel("meta")
@Data
@AllArgsConstructor
class Meta {
    @ApiModelProperty("状态码")
    private int status;
    @ApiModelProperty("状态信息")
    private String message;
}

/**
 * @author xyongfeng
 */
@ApiModel("返回json实体")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult<T> {
    @ApiModelProperty("json核心数据内容")
    private T data;
    @ApiModelProperty("meta")
    private Meta meta;
    /**
     * 全部默认
     */
    public JsonResult() {
        this.meta = new Meta(HttpStatus.OK.value(), "操作成功");
    }

    /**
     * 自定义data，status,message默认
     * @param data 输出的数据
     */
    public JsonResult(T data) {
        this.data = data;
        this.meta = new Meta(HttpStatus.OK.value(), "操作成功");
    }
    /**
     * 自定义status,message封装成json
     * @param status 状态码
     * @param message 回复的内容
     */
    public JsonResult(int status, String message) {
        this.meta = new Meta(status,message);
    }
    /**
     * 自定义data,status,message封装成json
     * @param data 输出的数据
     * @param status 状态码
     * @param message 回复的内容
     */
    public JsonResult(T data, int status, String message) {
        this.data = data;
        this.meta = new Meta(status,message);
    }


}
