package com.cakemonster.framework.builder.xml;

import com.alibaba.druid.pool.DruidDataSource;
import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.io.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载mybatis-config.xml配置文件
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class XMLConfigBuilder {

    private final Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    public Configuration parseConfig(InputStream inputStream)
        throws ParserConfigurationException, IOException, SAXException {

        // Create DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Create DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Parse XML InputStream using DOM
        Document document = builder.parse(inputStream);
        // <configuration>
        Element rootElement = document.getDocumentElement();
        // Select nodes using XPath (DOM)
        Element dataSource = (Element)rootElement.getElementsByTagName("dataSource").item(0);
        NodeList propertyNodes = dataSource.getElementsByTagName("property");

        Properties properties = new Properties();
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Element element = (Element)propertyNodes.item(i);
            String name = element.getAttribute("name");
            String value = element.getAttribute("value");
            properties.setProperty(name, value);
        }

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driverClass"));
        druidDataSource.setUrl(properties.getProperty("jdbcUrl"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(druidDataSource);

        // Mapper.xml parsing: Get path -- Byte input stream -- DOM parsing
        Element mappers = (Element)rootElement.getElementsByTagName("mappers").item(0);
        NodeList mapperNodes = mappers.getElementsByTagName("mapper");
        for (int i = 0; i < mapperNodes.getLength(); i++) {
            Element element = (Element)mapperNodes.item(i);
            String mapperPath = element.getAttribute("resource");
            InputStream mapperInputStream = Resources.getResourceAsSteam(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(mapperInputStream);
        }
        return configuration;
    }

}
