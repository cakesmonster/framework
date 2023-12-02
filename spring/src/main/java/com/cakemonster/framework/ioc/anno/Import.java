package com.cakemonster.framework.ioc.anno;

import java.lang.annotation.*;

/**
 * Import
 *
 * @author cakemonster
 * @date 2023/12/3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

    Class<?> value();
}
