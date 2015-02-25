package com.rodiontsev.maven.plugins.buildinfo.utils;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Date: 25/02/15
 * Time: 22:45
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class InfoWriter {
    private static final String DEFAULT_VALUE = "";

    public void write(Map<String, String> info, @Nullable List<String> properties, PropertyMapper mapper) {
        if (properties != null) {
            for (String propertyName : properties) {
                String propertyValue = mapper.mapProperty(propertyName);
                info.put(propertyName, StringUtils.isNotEmpty(propertyValue) ? propertyValue : DEFAULT_VALUE);
            }
        }
    }

}
