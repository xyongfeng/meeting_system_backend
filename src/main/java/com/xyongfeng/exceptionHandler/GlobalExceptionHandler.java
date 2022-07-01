package com.xyongfeng.exceptionHandler;

import com.xyongfeng.pojo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
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
     * @param response
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JsonResult methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {

        List<ObjectError> allErrors = e.getAllErrors();
        ObjectError error = allErrors.get(0);
        log.info(error.getDefaultMessage());
        return JsonResult.error(error.getDefaultMessage());
    }
}
