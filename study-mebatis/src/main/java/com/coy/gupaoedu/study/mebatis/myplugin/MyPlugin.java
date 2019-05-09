package com.coy.gupaoedu.study.mebatis.myplugin;

import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.GPResultHandler;
import com.coy.gupaoedu.study.mebatis.executor.GPExecutor;
import com.coy.gupaoedu.study.mebatis.plugin.Interceptor;
import com.coy.gupaoedu.study.mebatis.plugin.Intercepts;
import com.coy.gupaoedu.study.mebatis.plugin.Invocation;
import com.coy.gupaoedu.study.mebatis.plugin.Plugin;
import com.coy.gupaoedu.study.mebatis.plugin.Signature;

import java.util.Arrays;

/**
 * 自定义插件
 */
@Intercepts(value = {
        @Signature(type = GPExecutor.class, method = "query", args = {GPMappedStatement.class, Object.class, GPResultHandler.class})
})
public class MyPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        GPMappedStatement statement = (GPMappedStatement) invocation.getArgs()[0];
        Object[] parameter = (Object[]) invocation.getArgs()[1];
        GPResultHandler resultHandler = (GPResultHandler) invocation.getArgs()[2];
        GPExecutor executor = (GPExecutor) invocation.getTarget();

        System.out.println("插件输出：SQL：[" + statement.getSql() + "]");
        System.out.println("插件输出：Parameters：" + Arrays.toString(parameter));
        Object object = invocation.proceed();
        System.out.println("插件输出：Result：[" + object + "]");
        return object;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
