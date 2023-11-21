package com.cakemonster.framework.ioc.io;

import java.io.InputStream;

/**
 * ResourceLoader
 *
 * @author cakemonster
 * @date 2023/11/21
 */
public class ResourceLoader {
    public InputStream getResourceAsSteam(String path) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(path);
    }
}
