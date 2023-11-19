package com.cakemonster.framework.session;

/**
 * SqlSessionFactory
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public interface SqlSessionFactory {
    SqlSession openSession();
}
