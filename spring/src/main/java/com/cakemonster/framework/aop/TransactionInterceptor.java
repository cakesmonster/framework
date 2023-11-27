package com.cakemonster.framework.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * TransactionInterceptor
 *
 * @author cakemonster
 * @date 2023/11/26
 */
public class TransactionInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            System.out.println("transaction begin...");
            Object proceed = methodInvocation.proceed();
            System.out.println("transaction end...");
            return proceed;
        } finally {
            // close
        }
    }
}
