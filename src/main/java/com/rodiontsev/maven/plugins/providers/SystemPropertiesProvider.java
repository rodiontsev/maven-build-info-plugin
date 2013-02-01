package com.rodiontsev.maven.plugins.providers;

import com.rodiontsev.maven.plugins.BuildInfoMojo;
import com.rodiontsev.maven.plugins.InfoProvider;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 01.02.13
 * Time: 17:06
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class SystemPropertiesProvider implements InfoProvider {
    private static final String DEFAULT_VALUE = "";

    public boolean isActive(MavenProject project) {
        return true;
    }

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();

        List<String> properties = mojo.getSystemProperties();
        if (properties != null) {
            for (String property : properties) {
                info.put(property, System.getProperty(property, DEFAULT_VALUE));
            }
        }

        return info;
    }

}
