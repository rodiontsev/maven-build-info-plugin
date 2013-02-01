package com.rodiontsev.maven.plugins.providers;

import com.rodiontsev.maven.plugins.BuildInfoMojo;
import com.rodiontsev.maven.plugins.InfoProvider;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.maven.project.MavenProject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Date: 22.06.11
 * Time: 23:21
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class ProjectInfoProvider implements InfoProvider {

    public boolean isActive(MavenProject project) {
        return true;
    }

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();
        info.put("project.name", project.getName());
        info.put("project.version", project.getVersion());
        info.put("build.time", DateFormatUtils.format(new Date(), "d MMMM yyyy, HH:mm:ss ZZ", Locale.ENGLISH));
        return info;
    }

}
