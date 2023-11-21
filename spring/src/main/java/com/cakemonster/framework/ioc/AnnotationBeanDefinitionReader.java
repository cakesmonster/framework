package com.cakemonster.framework.ioc;

import com.cakemonster.framework.ioc.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * AnnotationBeanDefinitionReader
 *
 * @author cakemonster
 * @date 2023/11/21
 */
public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public AnnotationBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws Exception {
        InputStream inputStream = getResourceLoader().getResourceAsSteam(location);
        Properties properties = loadConfig(inputStream);
        scan(properties.getProperty("scanPackage"));
        autowired();
    }

    /**
     * 依赖注入
     */
    private void autowired() {

    }

    /**
     * 扫描
     *
     * @param scanPackage 扫描的包
     */
    private void scan(String scanPackage) {

    }

    private Properties loadConfig(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
