package com.coy.gupaoedu.study.mebatis.executor;

import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.GPResultHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * SQL执行器：包含SQL执行的全流程，如参数处理、SQL执行、结果处理
 *
 * @author chenck
 * @date 2019/5/6 20:57
 */
public interface GPExecutor {

    GPResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(GPMappedStatement ms, Object parameter, GPResultHandler resultHandler) throws SQLException;
}
