package com.cakemonster.framework.ioc;

import com.cakemonster.framework.ioc.anno.Component;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

    private final Map<String, BeanDefinition> registry = Maps.newConcurrentMap();

    public void scan(String scanPackage) {
        Set<BeanDefinition> beanDefinitions = findCandidateComponents(scanPackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = generateBeanName(beanDefinition);
            registry.put(beanName, beanDefinition);
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

    private String generateBeanName(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        String shortName = getShortName(beanClassName);
        return decapitalize(shortName);
    }

    private String getShortName(String className) {
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

    private String decapitalize(String name) {
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
