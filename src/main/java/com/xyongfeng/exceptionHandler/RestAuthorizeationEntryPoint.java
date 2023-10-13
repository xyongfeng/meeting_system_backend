package com.xyongfeng.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyongfeng.content.ResCode;
import com.xyongfeng.pojo.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效时访问接口时，自定义的返回结果
 *
 * @author xyongfeng
 */
@Component
public class RestAuthorizeationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonResult jsonResult = JsonResult.error(ResCode.ONLINE_ERROR.getCode(), "请登录再访问");

        out.write(new ObjectMapper().writeValueAsString(jsonResult));
        out.flush();
        out.close();
    }
}
