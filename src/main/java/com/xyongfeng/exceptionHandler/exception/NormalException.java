package com.xyongfeng.exceptionHandler.exception;

import com.xyongfeng.content.ResCode;

/**
 * @author xyongfeng
 */


public class NormalException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;


    public NormalException(int code, String message) {
        super(message);
        this.code = code;
    }

    public NormalException(String message) {
        this(ResCode.SYSTEM_ERROR.getCode(), message);
    }

    public NormalException(ResCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public NormalException(ResCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
