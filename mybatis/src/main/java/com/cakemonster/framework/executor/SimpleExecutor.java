package com.cakemonster.framework.executor;

import com.cakemonster.framework.mapping.BoundSql;
import com.cakemonster.framework.config.Configuration;
import com.cakemonster.framework.mapping.MappedStatement;
import com.cakemonster.framework.mapping.ParameterMapping;
import com.cakemonster.framework.parser.GenericTokenParser;
import com.cakemonster.framework.builder.ParameterMappingTokenHandler;
import com.google.common.base.Strings;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * SimpleExecutor
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class SimpleExecutor implements Executor {

    /**
     * TODO(hzq): params有问题
     *
     * @param configuration
     * @param mappedStatement
     * @param params
     * @param <E>
     * @return
     * @throws Exception
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params)
        throws Exception {
        Connection connection = configuration.getDataSource().getConnection();

        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // TODO(hzq): 有问题
        String parameterType = mappedStatement.getParameterType();
        Class<?> paramtertypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getProperty();
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, o);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();

        while (resultSet.next()) {
            // TODO(hzq): 有问题
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }
        return (List<E>)objects;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (!Strings.isNullOrEmpty(parameterType)) {
            return Class.forName(parameterType);
        }
        return null;
    }

    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parseSql, parameterMappings);
    }

}
