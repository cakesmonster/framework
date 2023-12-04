package com.cakemonster.framework.spring;

import com.cakemonster.framework.builder.xml.XMLConfigBuilder;
import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.io.Resources;
import com.cakemonster.framework.ioc.anno.FactoryBean;
import com.cakemonster.framework.session.DefaultSqlSessionFactory;
import com.cakemonster.framework.session.SqlSessionFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * SqlSessionFactoryBean
 *
 * @author cakemonster
 * @date 2023/12/2
 */
public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory> {

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            this.sqlSessionFactory = buildSqlSessionFactory();
        }
        return sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }

    private SqlSessionFactory buildSqlSessionFactory() {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = null;
        try {
            configuration = xmlConfigBuilder.parseConfig(resourceAsSteam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new DefaultSqlSessionFactory(configuration);
    }
}
