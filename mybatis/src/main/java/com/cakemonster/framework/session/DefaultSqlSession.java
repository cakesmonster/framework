package com.cakemonster.framework.session;

import com.cakemonster.framework.executor.Executor;
import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.mapping.MappedStatement;
import com.google.common.collect.Maps;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DefaultSqlSession
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    private final Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> list = this.executor.query(configuration, mappedStatement, params);
        return (List<E>)list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T)objects.get(0);
        } else {
            throw new RuntimeException("empty");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance =
            Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[] {mapperClass},
                (proxy, method, args) -> {
                    String methodName = method.getName();
                    String className = method.getDeclaringClass().getName();
                    String statementId = className + "." + methodName;
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        return selectList(statementId, args);
                    }
                    return selectOne(statementId, args);
                });
        return (T)proxyInstance;
    }

    private Object wrapCollection(final Object object) {
        if (object instanceof Collection) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        } else if (object != null && object.getClass().isArray()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("array", object);
            return map;
        }
        return object;
    }

}
