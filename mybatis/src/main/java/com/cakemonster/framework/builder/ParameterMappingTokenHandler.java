package com.cakemonster.framework.builder;

import com.cakemonster.framework.mapping.ParameterMapping;
import com.cakemonster.framework.parser.TokenHandler;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 参数映射
 *
 * @author cakemonster
 * @date 2023/11/18
 */
@Data
public class ParameterMappingTokenHandler implements TokenHandler {
    private List<ParameterMapping> parameterMappings = Lists.newCopyOnWriteArrayList();

    @Override
    public String handleToken(String content) {
        // content #{id} #{username}
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        return new ParameterMapping(content);
    }

}
