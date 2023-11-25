package com.cakemonster.framework.ioc.util;

import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * BeanNameUtil
 *
 * @author cakemonster
 * @date 2023/11/25
 */
public class BeanNameUtil {

    public static String generateBeanName(String className) {
        String shortName = getShortName(className);
        return decapitalize(shortName);
    }

    public static String generateBeanName(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        String shortName = getShortName(beanClassName);
        return decapitalize(shortName);
    }

    private static String getShortName(String className) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(className), "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(".");
        int nameEndIndex = className.indexOf("$$");
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace("$", ".");
        return shortName;
    }

    private static String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
