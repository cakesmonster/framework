package com.cakemonster.framework.mapping;

import lombok.Data;

/**
 * Mapper.xml里sql配置
 *
 * @author cakemonster
 * @date 2023/11/18
 */
@Data
public class MappedStatement {

    /**
     * namespace
     */
    private String id;
    /**
     * 返回值类型
     */
    private String resultType;
    /**
     * 参数值类型
     */
    private String parameterType;
    /**
     * sql语句
     */
    private String sql;

}
