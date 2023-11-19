package com.cakemonster.framework.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ParameterMapping
 *
 * @author cakemonster
 * @date 2023/11/18
 */
@Data
@AllArgsConstructor
public class ParameterMapping {

    private String property;

    private String jdbcType;

    private Class<?> typeClass;

    public ParameterMapping(String property) {
        this.property = property;
    }
}
