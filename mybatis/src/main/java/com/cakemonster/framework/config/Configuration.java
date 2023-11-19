package com.cakemonster.framework.config;

import com.cakemonster.framework.mapping.MappedStatement;
import com.google.common.collect.Maps;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置
 *
 * @author cakemonster
 * @date 2023/11/18
 */
@Data
public class Configuration {

    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * <namespace, SQL>
     */
    Map<String, MappedStatement> mappedStatementMap = Maps.newConcurrentMap();
}
