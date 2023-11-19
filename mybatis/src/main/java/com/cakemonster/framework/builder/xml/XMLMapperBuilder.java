package com.cakemonster.framework.builder.xml;

import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.mapping.MappedStatement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 加载mapper.xml文件
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        // Create DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Create DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Parse XML InputStream using DOM
        Document document = builder.parse(inputStream);
        // Get root element
        Element rootElement = document.getDocumentElement();
        // Extract namespace
        String namespace = rootElement.getAttribute("namespace");
        // Select nodes using XPath (DOM)
        NodeList selectNodes = rootElement.getElementsByTagName("select");
        for (int i = 0; i < selectNodes.getLength(); i++) {
            Element element = (Element)selectNodes.item(i);
            String id = element.getAttribute("id");
            String resultType = element.getAttribute("resultType");
            String parameterType = element.getAttribute("parameterType");
            String sqlText = element.getTextContent().trim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqlText);
            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);
        }
    }

}
