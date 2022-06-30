package com.xyongfeng.config.security;

import com.xyongfeng.service.AdminsService;
import com.xyongfeng.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security配置
 *
 * @author xyongfeng
 */
//@Configuration
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserService userService;

    @Resource
    private RestAuthorizeationEntryPoint restAuthorizeationEntryPoint;

    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtAuthencationTokenFilter jwtAuthencationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userService.getUserByUserName(username)).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用JWT,不需要csrf
        http.csrf()
                .disable()
                // 基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //允许登录访问
//                .antMatchers("/admin/login","/admin/logout")
//                .permitAll()
                // 除了上面所有请求都要认证
                .anyRequest()
                .authenticated()
                .and()
                // 关闭缓存
                .headers()
                .cacheControl();
        // 添加jwt,登录授权过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizeationEntryPoint);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "index.html",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**"
        );
    }
}
