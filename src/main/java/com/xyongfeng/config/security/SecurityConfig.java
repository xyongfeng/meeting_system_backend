package com.xyongfeng.config.security;

import com.xyongfeng.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Security配置
 * @author xyongfeng
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private AdminsService adminsService;

    @Resource
    private RestAuthorizeationEntryPoint restAuthorizeationEntryPoint;

    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> adminsService.getAdminByUserName(username);
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
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
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizeationEntryPoint);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/admins/login",
                "/admins/logout",
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
