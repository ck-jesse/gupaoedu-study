package com.coy.gupaoedu.study.mybatisplus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 接口版本号的处理器，暂时预留在这里
 */
public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionRequestMappingHandlerMapping.class);

    public static final String HANDLER_URL_PATTERN = "/api/%s%s"; // key pattern,such as: /api/1.1/product/detail

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class);
    }

    @Override
    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        // 1.是否有使用Feign定义接口
        HandlerMethod handlerMethod;
        if (handler instanceof String) {
            String beanName = (String) handler;
            handlerMethod = new RemoteServiceHandlerMethod(beanName,
                    getApplicationContext().getAutowireCapableBeanFactory(), method);
        } else {
            handlerMethod = new RemoteServiceHandlerMethod(handler, method);
        }
        return handlerMethod;
    }

    private ApiVersion getApiVersion(Class<?> currentClass, Method method) {
        final Class<?> interfaceClass = getApiInterface(currentClass);
        try {
            final Method interfaceMethod = interfaceClass.equals(currentClass) ? method
                    : interfaceClass.getMethod(method.getName(), method.getParameterTypes());

            // method's ApiVersion
            final ApiVersion methodApiVersion = interfaceMethod.getAnnotation(ApiVersion.class);

            // type's ApiVersion
            final ApiVersion typeApiVersion = interfaceClass.getAnnotation(ApiVersion.class);

            // method's ApiVersion > type's ApiVersion
            return Objects.nonNull(methodApiVersion) ? methodApiVersion : typeApiVersion;
        } catch (NoSuchMethodException | SecurityException e) {
            LOGGER.error("registerHandlerMethod reflect has error.");
            return null;
        }
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        final Class<?> currentClass = method.getDeclaringClass();
        final ApiVersion version = getApiVersion(currentClass, method);
        final ApiVersion apiVersion = Objects.nonNull(version) ? version : null;
        if (Objects.nonNull(apiVersion)) {
            registApiHandlerMethod(handler, method, mapping, apiVersion);
            return;
        }
        super.registerHandlerMethod(handler, method, mapping);
    }

    private static class RemoteServiceHandlerMethod extends HandlerMethod {

        private Method interfaceMethod;

        private Class<?> interfaceClass;

        public RemoteServiceHandlerMethod(Object bean, Method method) {
            super(bean, method);
            changeType();
        }

        public RemoteServiceHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes)
                throws NoSuchMethodException {
            super(bean, methodName, parameterTypes);
            changeType();
        }

        public RemoteServiceHandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
            super(beanName, beanFactory, method);
            changeType();
        }

        private void changeType() {
            // 兼容SpringMVC,Controller不需要接口,直接实现的方式;
            this.interfaceClass = getApiInterface(getMethod().getDeclaringClass());
            try {
                interfaceMethod = interfaceClass.getMethod(getMethod().getName(), getMethod().getParameterTypes());
                MethodParameter[] params = super.getMethodParameters();
                for (int i = 0; i < params.length; i++) {
                    params[i] = new RemoteServiceMethodParameter(params[i]);
                }
            } catch (NoSuchMethodException | SecurityException e) {
                LOGGER.error("changeType reflect has error.");
            }
        }

        private class RemoteServiceMethodParameter extends MethodParameter {

            private volatile Annotation[] parameterAnnotations;

            public RemoteServiceMethodParameter(MethodParameter methodParameter) {
                super(methodParameter);
            }

            @Override
            public Annotation[] getParameterAnnotations() {
                if (Objects.isNull(this.parameterAnnotations)) {
                    if (Objects.nonNull(RemoteServiceHandlerMethod.this.interfaceMethod)) {
                        Annotation[][] annotationArray =
                                RemoteServiceHandlerMethod.this.interfaceMethod.getParameterAnnotations();
                        setParameterAnnotations(annotationArray);
                    } else {
                        this.parameterAnnotations = super.getParameterAnnotations();
                    }
                }
                return this.parameterAnnotations;
            }

            private void setParameterAnnotations(Annotation[][] annotationArray) {
                if (this.getParameterIndex() >= 0 && this.getParameterIndex() < annotationArray.length) {
                    this.parameterAnnotations = annotationArray[this.getParameterIndex()];
                } else {
                    this.parameterAnnotations = new Annotation[0];
                }
            }

            @Override
            public boolean equals(Object other) {
                return super.equals(other);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

        }

        @Override
        public boolean equals(Object other) {
            return super.equals(other);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }

    private void registApiHandlerMethod(Object handler, Method method, RequestMappingInfo mapping,
                                        ApiVersion apiVersion) {
        PatternsRequestCondition patternsCondition = mapping.getPatternsCondition();
        RequestMethodsRequestCondition methodsCondition = mapping.getMethodsCondition();
        if (Objects.isNull(patternsCondition) || Objects.isNull(methodsCondition)
                || CollectionUtils.isEmpty(patternsCondition.getPatterns())
                || CollectionUtils.isEmpty(methodsCondition.getMethods())) {
            return;
        }
        Iterator<String> patternIterator = patternsCondition.getPatterns().iterator();
        Iterator<RequestMethod> methodIterator = methodsCondition.getMethods().iterator();
        final List<String> patterns = new ArrayList<String>();
        while (patternIterator.hasNext() && methodIterator.hasNext()) {
            String patternItem = patternIterator.next();
            String urlPattern = String.format(HANDLER_URL_PATTERN, apiVersion.value(), patternItem);
            patterns.add(urlPattern);
        }
        final PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(
                CollectionUtils.isEmpty(patterns) ? null : patterns.toArray(new String[patterns.size()]));
        final RequestMappingInfo requestMappingInfo =
                new RequestMappingInfo(mapping.getName(), patternsRequestCondition, mapping.getMethodsCondition(),
                        mapping.getParamsCondition(), mapping.getHeadersCondition(), mapping.getConsumesCondition(),
                        mapping.getProducesCondition(), mapping.getMethodsCondition());
        super.registerHandlerMethod(handler, method, requestMappingInfo);
    }

    private static Class<?> getApiInterface(Class<?> beanType) {
        final Optional optional = Optional.build().of(beanType);

        //暂时没有用feign
//        filterApiInterface(beanType, FeignClient.class, optional);
        return optional.get();
    }

    private static void filterApiInterface(Class<?> beanType, Class<? extends Annotation> annotationClass,
                                           Optional optional) {
        if (beanType.isAnnotationPresent(annotationClass)) {
            optional.of(beanType);
            return;
        }
        final Class<?>[] interfaces = beanType.getInterfaces();
        for (Class<?> currentInterface : interfaces)
            filterApiInterface(currentInterface, annotationClass, optional);
    }

    private static class Optional {

        private Class<?> beanType;

        private Optional() {

        }

        public static Optional build() {
            return new Optional();
        }

        public Optional of(Class<?> beanType) {
            this.beanType = beanType;
            return this;
        }

        public Class<?> get() {
            return this.beanType;
        }
    }

}
