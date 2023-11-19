package com.cakemonster.framework.service;

import com.cakemonster.framework.mapper.UserDao;
import com.cakemonster.framework.model.User;
import com.cakemonster.framework.io.Resources;
import com.cakemonster.framework.session.SqlSession;
import com.cakemonster.framework.session.SqlSessionFactory;
import com.cakemonster.framework.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * TestMybatis
 *
 * @author cakemonster
 * @date 2023/11/17
 */
public class TestMybatis {

    public static void test() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setName("张三");
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User user1 = userDao.findByCondition(user);
        System.out.println(user1);

        //        List<User> all = userDao.findAll();
        //        for (User u : all) {
        //            System.out.println(u);
        //        }
    }

    public static void main(String[] args) throws Exception {
        test();
    }
}
