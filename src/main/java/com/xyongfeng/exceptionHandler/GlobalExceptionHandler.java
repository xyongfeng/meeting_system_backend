package com.xyongfeng.exceptionHandler;

import com.xyongfeng.util.JsonResult;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestControllerAdvice
@Log
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JsonResult<Object> MethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response){

        List<ObjectError> allErrors = e.getAllErrors();
        ObjectError error = allErrors.get(0);
        log.info(error.getDefaultMessage());
        return new JsonResult<>(HttpStatus.BAD_REQUEST.value(),error.getDefaultMessage());
    }
}
