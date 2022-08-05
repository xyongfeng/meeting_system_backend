package com.xyongfeng.exceptionHandler;

import com.xyongfeng.pojo.JsonResult;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 全局错误抓取
 * @author xyongfeng
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 抓取字段错误
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
    public JsonResult dateTimeParseException(DateTimeParseException e){

        return JsonResult.error(String.format("%s: 时间格式错误 格式为:yyyy-MM-dd HH:mm:ss",e.getParsedString()));
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public JsonResult expiredJwtException(ExpiredJwtException e){
        return JsonResult.error("登录认证过期，请重新登录");
    }
}
