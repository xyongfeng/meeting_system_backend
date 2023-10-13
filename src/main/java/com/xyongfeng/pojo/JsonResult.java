package com.xyongfeng.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xyongfeng.content.ResCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author xyongfeng
 */
@ApiModel("返回json实体")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("状态信息")
    private String message;
    @ApiModelProperty("json核心数据内容")
    private Object data;

    public JsonResult(int i) {
        this.code = i;

    }

    public JsonResult(int i, String message) {
        this.code = i;
        this.message = message;
    }

    public JsonResult(int i, Object data) {
        this.code = i;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param message
     * @return
     */
    public static JsonResult success(String message) {
        return new JsonResult(ResCode.SUCCESS.getCode(), message);
    }

    public static JsonResult success() {
        return new JsonResult(ResCode.SUCCESS.getCode());
    }

    public static JsonResult success(Object data) {
        return new JsonResult(ResCode.SUCCESS.getCode(), data);
    }

    /**
     * 成功返回结果
     *
     * @param message
     * @param data
     * @return
     */
    public static JsonResult success(String message, Object data) {
        return new JsonResult(ResCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回结果
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static JsonResult success(int code, String message, Object data) {
        return new JsonResult(code, message, data);
    }

    public static JsonResult success(int code, String message) {
        return new JsonResult(code, message);
    }

    /**
     * 失败返回结果
     *
     * @param message
     * @return
     */
    public static JsonResult error(String message) {
        return new JsonResult(ResCode.SYSTEM_ERROR.getCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param message
     * @param data
     * @return
     */
    public static JsonResult error(String message, Object data) {
        return new JsonResult(ResCode.SYSTEM_ERROR.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static JsonResult error(int code, String message, Object data) {
        return new JsonResult(code, message, data);
    }

    public static JsonResult error(int code, String message) {
        return new JsonResult(code, message);
    }
}
