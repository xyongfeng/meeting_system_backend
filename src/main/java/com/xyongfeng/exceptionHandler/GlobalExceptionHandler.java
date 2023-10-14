package com.xyongfeng.exceptionHandler;

import com.xyongfeng.exceptionHandler.exception.NormalException;
import com.xyongfeng.pojo.JsonResult;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 全局错误抓取
 *
 * @author xyongfeng
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 抓取字段错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult methodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<ObjectError> allErrors = e.getAllErrors();
        ObjectError error = allErrors.get(0);
        log.info(error.getDefaultMessage());
        return JsonResult.error(error.getDefaultMessage());
    }
//    @ExceptionHandler(SQLException.class)
//    public JsonResult sqlException(SQLException e){
//        return JsonResult.error("数据库异常,操作失败");
//    }

    @ExceptionHandler(DateTimeParseException.class)
    public JsonResult dateTimeParseException(DateTimeParseException e) {

        return JsonResult.error(String.format("%s: 时间格式错误 格式为:yyyy-MM-dd HH:mm:ss", e.getParsedString()));
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public JsonResult socketTimeoutException(SocketTimeoutException e) {
        return JsonResult.error("请求超时，请稍后再试");
    }

    @ExceptionHandler({ConnectException.class, HttpServerErrorException.class, HttpClientErrorException.class})
    public JsonResult connectException(Exception e) {
        return JsonResult.error("人脸识别服务未启动，请通知管理员");
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public JsonResult fileSizeLimitExceededException(FileSizeLimitExceededException e) {
        return JsonResult.error("文件上传失败");
    }

    @ExceptionHandler(NormalException.class)
    public JsonResult normalException(NormalException e) {
        return JsonResult.error(e.getCode(), e.getMessage());
    }


}
