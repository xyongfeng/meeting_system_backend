package com.xyongfeng.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyongfeng.content.ResCode;
import com.xyongfeng.pojo.JsonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当访问接口没有权限时，自定义返回结果
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonResult jsonResult = JsonResult.error(ResCode.FORBIDDEN_ERROR.getCode(), "权限不足");
        out.write(new ObjectMapper().writeValueAsString(jsonResult));
        out.flush();
        out.close();
    }
}
