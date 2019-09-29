package com.coy.gupaoedu.study.security.servlet;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * servlet security 相关实现
 *
 * @author chenck
 * @date 2019/9/29 22:54
 */
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    /**
     * 拦截请求并进行认证，如果没有认证，则会跳转到认证页面（登录页面）
     */
    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        // 设置用户名和密码
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
        return manager;
    }
}
