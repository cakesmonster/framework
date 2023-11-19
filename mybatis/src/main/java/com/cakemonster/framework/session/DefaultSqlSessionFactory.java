package com.cakemonster.framework.session;

import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.executor.SimpleExecutor;

/**
 * DefaultSqlSessionFactory
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration, new SimpleExecutor());
    }
}
