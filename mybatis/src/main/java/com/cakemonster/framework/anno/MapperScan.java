package com.cakemonster.framework.anno;

import com.cakemonster.framework.ioc.anno.Import;
import com.cakemonster.framework.spring.MapperScannerRegistrar;

import java.lang.annotation.*;

/**
 * MapperScan
 *
 * @author cakemonster
 * @date 2023/12/3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
public @interface MapperScan {

    // TODO(hzq): 不实现数组了,费劲
    String value() default "";
}
