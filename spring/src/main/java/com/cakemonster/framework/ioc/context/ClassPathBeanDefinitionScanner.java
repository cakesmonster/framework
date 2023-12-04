package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.anno.Component;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationAttributes;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.cakemonster.framework.ioc.util.BeanNameUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * ClassPathBeanDefinitionScanner
 *
 * @author cakemonster
 * @date 2023/11/21
 */
@Getter
public class ClassPathBeanDefinitionScanner {

    private final AbstractBeanFactory beanFactory;

    public ClassPathBeanDefinitionScanner(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void scan(String... scanPackages) {
        doScan(scanPackages);
    }

    protected Set<BeanDefinition> doScan(String... scanPackages) {
        Set<BeanDefinition> candidates = Sets.newHashSet();
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> beanDefinitions = findCandidateComponents(scanPackage);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String beanName = BeanNameUtil.generateBeanName(beanDefinition);
                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                // 接口
                Class<?>[] interfaces = beanDefinition.getMetaData().getInterfaces();
                if (interfaces == null || interfaces.length == 0) {
                    candidates.add(beanDefinition);
                    continue;
                }
                for (Class<?> anInterface : interfaces) {
                    String interfaceTypeName = anInterface.getTypeName();
                    String interfaceBeanName = BeanNameUtil.generateBeanName(interfaceTypeName);
                    beanFactory.registerBeanDefinition(interfaceBeanName, beanDefinition);
                }
                candidates.add(beanDefinition);
            }
        }
        return candidates;
    }

    private Set<BeanDefinition> findCandidateComponents(String scanPackage) {
        Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
        List<String> resources = Lists.newArrayList();
        getResources(scanPackage, resources);
        for (String resource : resources) {
            // 1. 获取meta
            AnnotationMetaData metadataReader = getMetadataReader(resource);
            // 2. 判断是否有Component注解或者继承了Component注解
            if (!isCandidateComponent(metadataReader)) {
                continue;
            }
            // 3. 创建BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(metadataReader);
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    protected boolean isCandidateComponent(AnnotationMetaData metaData) {
        Annotation[] annotations = metaData.getAnnotations();
        // 遍历注解，检查是否包含@Component或@Component的实现类
        for (Annotation annotation : annotations) {
            if (isComponentAnnotation(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    protected AnnotationMetaData getMetadataReader(String resource) {
        String className = resource.replaceAll("/", ".");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        AnnotationMetaData metaData = new AnnotationMetaData();
        Class<?>[] interfaces = clazz.getInterfaces();
        Annotation[] annotations = clazz.getAnnotations();
        metaData.setBeanClassName(className);
        metaData.setClazz(clazz);
        metaData.setInterfaces(interfaces);
        metaData.setAnnotations(annotations);
        metaData.setResource(resource);

        for (Annotation annotation : annotations) {
            AnnotationAttributes annotationAttributes = new AnnotationAttributes();
            extractAnnotationAttributes(annotationAttributes, annotation);
            metaData.getAttributesMap().put(annotation.annotationType().getName(), annotationAttributes);
        }
        return metaData;
    }

    private void extractAnnotationAttributes(AnnotationAttributes annotationAttributes, Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        for (Method method : annotationType.getDeclaredMethods()) {
            try {
                Object value = method.invoke(annotation);
                annotationAttributes.put(method.getName(), value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isComponentAnnotation(Class<? extends Annotation> annotationType) {
        // 注解本身是否是@Component
        if (annotationType.equals(Component.class)) {
            return true;
        }
        // 检查注解类上是否有@Component注解
        Component annotation = annotationType.getAnnotation(Component.class);
        return annotation != null;
    }

    private void getResources(String scanPackage, List<String> allResources) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        Objects.requireNonNull(resource);
        String scanPackagePath = resource.getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);
        File[] files = pack.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            // 子package
            if (file.isDirectory()) {
                getResources(scanPackage + "." + file.getName(), allResources);
            } else if (file.getName().endsWith(".class")) {
                String name = scanPackage + "." + file.getName().replaceAll(".class", "");
                allResources.add(name);
            }
        }
    }

}
