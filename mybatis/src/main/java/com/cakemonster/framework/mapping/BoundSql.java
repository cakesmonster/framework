package com.cakemonster.framework.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * BoundSql
 *
 * @author cakemonster
 * @date 2023/11/18
 */
@Data
@AllArgsConstructor
public class BoundSql {

    /**
     * 解析过的sql
     */
    private String sqlText;

    /**
     * 参数映射
     */
    private List<ParameterMapping> parameterMappingList;

}
