package com.cakemonster.framework.ioc.bean;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * PropertyValues
 *
 * @author cakemonster
 * @date 2023/11/25
 */
@Getter
public class PropertyValues {

    private List<PropertyValue> propertyValues = Lists.newCopyOnWriteArrayList();

    public void addPropertyValue(PropertyValue propertyValue) {
        propertyValues.add(propertyValue);
    }
}
