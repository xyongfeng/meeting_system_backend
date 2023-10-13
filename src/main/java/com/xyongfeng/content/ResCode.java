package com.xyongfeng.content;

/**
 * 返回状态码
 */
public enum ResCode {
    SUCCESS(200, "ok"),
    NOT_FOUND_ERROR(404, "请求数据不存在"),
    FORBIDDEN_ERROR(403, "权限不足"),
    ONLINE_ERROR(401, "未登录"),
    SYSTEM_ERROR(500, "运行错误");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
