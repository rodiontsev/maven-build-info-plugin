package com.rodiontsev.tools.maven.plugins;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.maven.project.MavenProject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 22.06.11
 * Time: 23:21
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class ProjectInfoProvider implements InfoProvider {

    public Map<String, String> getInfo(MavenProject project) {
        Date date = new Date();
        Map<String, String> info = new LinkedHashMap<String, String>();
        info.put("project.name", project.getName());
        info.put("project.version", project.getVersion());
        info.put("build.time", DateFormatUtils.format(date, "d MMMM yyyy, HH:mm:ss ZZ"));
        return info;
    }

}
