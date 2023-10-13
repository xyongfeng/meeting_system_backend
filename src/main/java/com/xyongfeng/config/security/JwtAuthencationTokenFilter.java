package com.xyongfeng.config.security;

import com.xyongfeng.content.ResCode;
import com.xyongfeng.exceptionHandler.exception.NormalException;
import com.xyongfeng.util.JwtTokenUtil;
import com.xyongfeng.util.MyUtil;
import com.xyongfeng.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 为token执行登录的过滤器
 *
 * @author xyongfeng
 */

public class JwtAuthencationTokenFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);

        if (null != authHeader && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length()).strip();
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            // token存在但未登录
            if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                // 查库获取用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 验证token是否有效，重新设置用户对象
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    // 如果当前id对应token不相等，则说明已经有人在线上了
                    Integer userId = MyUtil.getUsers().getId();
                    String redisToken = redisUtil.getUserOnline(userId);
                    // 当redisToken为null，则代表当前用户没人在线，可以直接登录
                    if (redisToken != null && !authToken.equals(redisToken)) {
                        resolver.resolveException(request, response, null, new NormalException(ResCode.ONLINE_ERROR, "该用户已在其他地方登录"));
                        return;
                    }
                    // 记录当前token至redis
                    redisUtil.putUserOnline(userId, authToken);
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
