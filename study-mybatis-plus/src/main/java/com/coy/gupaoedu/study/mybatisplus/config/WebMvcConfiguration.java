package com.coy.gupaoedu.study.mybatisplus.config;

import com.coy.gupaoedu.study.mybatisplus.common.exception.BusinessException;
import com.coy.gupaoedu.study.mybatisplus.exception.GlobalHandlerExceptionResolver;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author chenck
 * @date 2019/9/10 17:55
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private HttpMessageConverters httpMessageConverters;

    // 1.异常国际化处理
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Messages messages() throws IOException {
        Resource[] messageResources =
                new PathMatchingResourcePatternResolver().getResources("classpath*:messages/*.properties");
        Messages messages = new Messages();
        String[] baseNames = new String[messageResources.length];
        for (int i = 0, messageResourcesLength = messageResources.length; i < messageResourcesLength; i++) {
            Resource messageResource = messageResources[i];
            String filename = messageResource.getFilename();
            baseNames[i] = "messages/" + filename.substring(0, filename.indexOf('.'));
        }
        messages.setBasenames(baseNames);
        return messages;
    }

    // 2. HttpMessageConverter加载
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.addAll(httpMessageConverters.getConverters());
    }

    // 3. Jsr303支持
    @Override
    public Validator getValidator() {
        try {
            LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
            validator.setProviderClass(HibernateValidator.class);
            validator.setValidationMessageSource(messages());
            validator.afterPropertiesSet();
            return validator;
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        // 6.1. 自定义异常处理放在最前处理
        exceptionResolvers.add(new GlobalHandlerExceptionResolver());

        // 6.2. 设置Spring ExceptionResolver进chain.
        exceptionResolvers.add(new ExceptionHandlerExceptionResolver());
        exceptionResolvers.add(new ResponseStatusExceptionResolver());
        exceptionResolvers.add(new DefaultHandlerExceptionResolver());
    }

    // 8.版本支持
    @Override
    public RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new VersionRequestMappingHandlerMapping();
    }

}
