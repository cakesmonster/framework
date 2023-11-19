package com.cakemonster.framework.executor;

import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.mapping.MappedStatement;

import java.util.List;

/**
 * Executor
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
