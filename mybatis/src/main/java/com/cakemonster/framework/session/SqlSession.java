package com.cakemonster.framework.session;

import java.util.List;

/**
 * SqlSession
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public interface SqlSession {

    /**
     * 查询所有
     *
     * @param statementId
     * @param params
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    /**
     * 根据条件查询单个
     *
     * @param statementId
     * @param params
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;

    /**
     * 为Dao接口生成代理实现类
     *
     * @param mapperClass
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<?> mapperClass);

}
