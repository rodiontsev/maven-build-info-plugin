package com.rodiontsev.maven.plugins;

import org.apache.maven.project.MavenProject;

import java.util.Map;

/**
 * Date: 5/17/11
 * Time: 2:55 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public interface InfoProvider {

    boolean isActive(MavenProject project);

    Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo);

}
