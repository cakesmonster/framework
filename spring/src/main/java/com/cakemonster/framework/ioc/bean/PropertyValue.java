package com.cakemonster.framework.ioc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PropertyValue
 *
 * @author cakemonster
 * @date 2023/11/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyValue {

    private String name;

    private Object value;
}
