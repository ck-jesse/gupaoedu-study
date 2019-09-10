package com.coy.gupaoedu.study.mybatisplus.exception;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;
import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {

    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandlerExceptionResolver.class);


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        if (null == e) {
            return null;
        }
        Throwable tx = e;
        DataResponse<Object> httpResponse = new DataResponse<>();
        boolean isBizExcepiton = false;
        while (tx != null) {
            if (tx instanceof BusinessException) {
                BusinessException be = (BusinessException) tx;
                httpResponse.setCode(be.getErrorCode());
                httpResponse.setMsg(be.getErrorMessage());
                isBizExcepiton = true;
                break;
            }
            // 针对 SpringMVC 中入参bean验证的异常的处理
            // 【bean验证原理分析如下：】
            // DispatcherServlet.doDispatch -> RequestMappingHandlerAdapter.handleInternal ->
            // HandlerMethodArgumentResolverComposite 参数解析器组合（组合模式）
            // HandlerMethodReturnValueHandlerComposite 返回值处理器组合（组合模式）
            // ServletInvocableHandlerMethod.invokeAndHandle
            // InvocableHandlerMethod.invokeForRequest
            // InvocableHandlerMethod.getMethodArgumentValues -> 获取参数值，会根据参数获取其支持的参数化解析器 HandlerMethodArgumentResolverComposite
            // HandlerMethodArgumentResolverComposite.resolveArgument -> 获取具体的解析器并执行参数解析 HandlerMethodArgumentResolver.resolveArgument
            // RequestResponseBodyMethodProcessor.resolveArgument -> 执行参数解析
            // AbstractMessageConverterMethodArgumentResolver.validateIfApplicable -> 验证是否适用，其中对设置了 @Validated 或 @Valid 注解的方法进行参数验证
            // DataBinder.validate -> 在数据绑定对象上调用验证器对bean进行验证
            // org.springframework.validation.SmartValidator.validate -> spring扩展后的验证器
            // org.springframework.validation.Validator.validate -> spring扩展后的验证器
            // javax.validation.Validator.validate -> 最终调用JSR-303规范的Validator进行bean验证
            if (tx instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException exception = (MethodArgumentNotValidException) tx;
                httpResponse.setCode(ResponseCodeEnum.ERROR_PARAM.getCode());
                FieldError fieldError = exception.getBindingResult().getFieldError();
                if (null != fieldError) {
                    httpResponse.setMsg(fieldError.getDefaultMessage());
                } else {
                    httpResponse.setMsg(tx.getMessage());
                }
                isBizExcepiton = true;
                break;
            }
            tx = tx.getCause();
        }

        if (!isBizExcepiton) {
            LOGGER.info(e.getMessage(), e);
            httpResponse.setCode(ResponseCodeEnum.ERROR.getCode());
            httpResponse.setMsg("系统异常， 请稍后重试... ");
        } else {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            response.setContentType("application/json;charset=utf-8");
            String json = JSON.toJSONString(httpResponse);
            // 清空并重置缓冲器
            response.resetBuffer();
            response.getOutputStream().write(json.getBytes());
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }
        return new ModelAndView();

    }

}
