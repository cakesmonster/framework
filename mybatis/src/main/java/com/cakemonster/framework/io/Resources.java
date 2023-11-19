package com.cakemonster.framework.io;

import java.io.InputStream;

/**
 * Resources
 *
 * @author cakemonster
 * @date 2023/11/18
 */
public class Resources {

    public static InputStream getResourceAsSteam(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
