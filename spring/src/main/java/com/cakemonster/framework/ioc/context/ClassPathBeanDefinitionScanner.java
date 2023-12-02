package com.cakemonster.framework.ioc.context;

import com.cakemonster.framework.ioc.anno.Component;
import com.cakemonster.framework.ioc.bean.BeanDefinition;
import com.cakemonster.framework.ioc.factory.AbstractBeanFactory;
import com.cakemonster.framework.ioc.meta.AnnotationMetaData;
import com.cakemonster.framework.ioc.util.BeanNameUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> beanDefinitions = findCandidateComponents(scanPackage);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String beanName = BeanNameUtil.generateBeanName(beanDefinition);
                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                // 接口
                Class<?>[] interfaces = beanDefinition.getMetaData().getInterfaces();
                if (interfaces == null || interfaces.length == 0) {
                    continue;
                }
                for (Class<?> anInterface : interfaces) {
                    String interfaceTypeName = anInterface.getTypeName();
                    String interfaceBeanName = BeanNameUtil.generateBeanName(interfaceTypeName);
                    beanFactory.registerBeanDefinition(interfaceBeanName, beanDefinition);
                }
            }
        }
    }

    private Set<BeanDefinition> findCandidateComponents(String scanPackage) {
        Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
        List<String> resources = Lists.newArrayList();
        getResources(scanPackage, resources);
        for (String resource : resources) {
            // 1. 获取meta
            AnnotationMetaData metadataReader = getMetadataReader(resource);
            // 2. 判断是否有Component注解或者继承了Component注解
            if (!filterHasComponentAnnotation(metadataReader)) {
                continue;
            }
            // 3. 创建BeanDefinition
            BeanDefinition beanDefinition = new BeanDefinition(metadataReader);
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    private AnnotationMetaData getMetadataReader(String resource) {
        String className = resource.replaceAll("/", ".");
        try {
            AnnotationMetaData metaData = new AnnotationMetaData();
            Class<?> clazz = Class.forName(className);
            Class<?>[] interfaces = clazz.getInterfaces();
            Annotation[] annotations = clazz.getAnnotations();
            metaData.setBeanClassName(className);
            metaData.setClazz(clazz);
            metaData.setInterfaces(interfaces);
            metaData.setAnnotations(annotations);
            metaData.setResource(resource);
            return metaData;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private boolean filterHasComponentAnnotation(AnnotationMetaData metaData) {
        Annotation[] annotations = metaData.getAnnotations();
        // 遍历注解，检查是否包含@Component或@Component的实现类
        for (Annotation annotation : annotations) {
            if (isComponentAnnotation(annotation.annotationType())) {
                return true;
            }
        }
        return false;
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
