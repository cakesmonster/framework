package com.cakemonster.framework.aop;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * AdvisedSupport
 *
 * @author cakemonster
 * @date 2023/11/28
 */
@Data
public class AdvisedSupport {

    private TargetSource targetSource;

    private MethodInterceptor methodInterceptor;

}
