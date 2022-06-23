package com.xyongfeng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import springfox.documentation.spring.web.json.Json;

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
public class JsonResult {
    @ApiModelProperty("json核心数据内容")
    private Object data;
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
     *
     * @param data 输出的数据
     */
    public JsonResult(Object data) {
        this.data = data;
        this.meta = new Meta(HttpStatus.OK.value(), "操作成功");
    }

    /**
     * 自定义status,message封装成json
     *
     * @param status  状态码
     * @param message 回复的内容
     */
    public JsonResult(int status, String message) {
        this.meta = new Meta(status, message);
    }

    /**
     * 自定义data,status,message封装成json
     *
     * @param data    输出的数据
     * @param status  状态码
     * @param message 回复的内容
     */
    public JsonResult(Object data, int status, String message) {
        this.data = data;
        this.meta = new Meta(status, message);
    }

    /**
     * 成功返回结果
     *
     * @param message
     * @return
     */
    public static JsonResult success(String message) {
        return new JsonResult(200, message);
    }

    /**
     * 成功返回结果
     *
     * @param message
     * @param data
     * @return
     */
    public static JsonResult success(String message, Object data) {
        return new JsonResult(data, 200, message);
    }

    /**
     * 成功返回结果
     * @param status
     * @param message
     * @param data
     * @return
     */
    public static JsonResult success(int status, String message, Object data) {
        return new JsonResult(data, status, message);
    }

    public static JsonResult success(int status, String message) {
        return new JsonResult(status, message);
    }
    /**
     * 失败返回结果
     *
     * @param message
     * @return
     */
    public static JsonResult error(String message) {
        return new JsonResult(500, message);
    }

    /**
     * 失败返回结果
     *
     * @param message
     * @param data
     * @return
     */
    public static JsonResult error(String message, Object data) {
        return new JsonResult(data, 500, message);
    }

    /**
     * 失败返回结果
     * @param status
     * @param message
     * @param data
     * @return
     */
    public static JsonResult error(int status, String message, Object data) {
        return new JsonResult(data, status, message);
    }

    public static JsonResult error(int status, String message) {
        return new JsonResult(status, message);
    }
}
