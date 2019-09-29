package com.coy.gupaoedu.study.security.servlet;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 对请求执行 filter chain 过滤器链的调用，
 * WebAsyncManagerIntegrationFilter
 * SecurityContextPersistenceFilter
 * HeaderWriterFilter
 * CsrfFilter
 * LogoutFilter
 * UsernamePasswordAuthenticationFilter
 * DefaultLoginPageGeneratingFilter
 * DefaultLogoutPageGeneratingFilter
 * BasicAuthenticationFilter
 * RequestCacheAwareFilter
 * SecurityContextHolderAwareRequestFilter
 * AnonymousAuthenticationFilter
 * SessionManagementFilter
 * ExceptionTranslationFilter
 * FilterSecurityInterceptor
 *
 * @author chenck
 * @date 2019/9/29 23:19
 */
@Configuration
public class WebSecurityConfigurerAdapterDemo extends WebSecurityConfigurerAdapter {

    /**
     * 重写configure方法，对请求身份认证进行配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest() // 对应用程序的任何请求都需要对用户进行身份验证
                .authenticated()
                .and()
                .formLogin() // 允许表单的登录进行身份验证
//                .loginPage("/hello")// 因为没有页面所以直接写controller中的hello方法
//                .permitAll()
                .and()
                .httpBasic()// 允许用户使用HTTP Basic身份验证进行身份验证
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    }
}
