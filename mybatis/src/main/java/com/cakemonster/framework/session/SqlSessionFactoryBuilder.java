package com.cakemonster.framework.session;

import com.cakemonster.framework.builder.xml.XMLConfigBuilder;
import com.cakemonster.framework.config.Configuration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSessionFactoryBuilder
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);
        return new DefaultSqlSessionFactory(configuration);
    }

}
