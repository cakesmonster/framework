package com.cakemonster.framework.mapper;

import com.cakemonster.framework.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll() throws Exception;

    User findByCondition(User user) throws Exception;

}
