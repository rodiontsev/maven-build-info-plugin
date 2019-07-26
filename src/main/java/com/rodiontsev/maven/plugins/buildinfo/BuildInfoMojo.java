/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rodiontsev.maven.plugins.buildinfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * This plugin generates the build-info file which might contain
 * - build date
 * - build version
 * - source revision (Git, Mercurial or Subversion)
 * - system properties (user.name, java.vm.vendor, java.vm.version, java.vm.name, os.name, os.version, os.arch, etc.)
 *
 * If you include this file in the WAR or EAR file,
 * you will not waste your time trying to figure out a version that is deployed to a server.
 *
 * Date: 5/13/2011
 * Time: 12:00
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 *
 * @goal extract
 * @phase prepare-package
 */
public class BuildInfoMojo extends AbstractMojo {

    /**
     * The Maven Project
     *
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
    private MavenProject project;

    /**
     * The name of the generated file
     *
     * @parameter default-value="build.info"
     */
    private String filename;

    /**
     * The name of the directory
     *
     * @parameter default-value=""
     */
    private String fileDir;

    /**
     * Project properties which you would like to include in the generated file.
     *
     * project[.parent].id
     * project[.parent].groupId
     * project[.parent].artifactId
     * project[.parent].version
     * project[.parent].name
     * project[.parent].description
     * project[.parent].modelVersion
     * project[.parent].inceptionYear
     * project[.parent].packaging
     * project[.parent].url
     *
     * @parameter
     */
    private List<String> projectProperties;

    /**
     * Properties declared within the project itself
     *
     * @parameter
     */
    private List<String> declaredProperties;

    /**
     * System properties, like user.name, java.vm.vendor, java.vm.version, os.name, os.version, etc.,
     * which you would like to include in the generated file
     *
     * @parameter
     */
    private List<String> systemProperties;

    /**
     * Environment variables which you would like to include in the generated file
     *
     * @parameter
     */
    private List<String> environmentVariables;

    /**
     * The pattern to use to format the date
     *
     * @parameter default-value="d MMMM yyyy, HH:mm:ss ZZ"
     */
    private String dateTimePattern;

    /**
     * Include info from VCS in the generated file
     *
     * @parameter default-value="true"
     */
    private boolean includeVcsInfo;


    public void execute() throws MojoExecutionException, MojoFailureException {
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (InfoProvider provider : ServiceLoader.load(InfoProvider.class)) {
            map.putAll(provider.getInfo(project, this));
        }

        File buildDir = getBuildDir();

        File file = new File(buildDir, filename);

        Writer out = null;
        try {
            // we may not have target/ yet
            if (!buildDir.exists()) {
                boolean buildDirectoryCreated = buildDir.mkdir();
                if (buildDirectoryCreated) {
                    getLog().info("The build directory was created");
                }
            }

            boolean infoFileCreated = file.createNewFile();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                out.write(entry.getKey());
                out.write(" = ");
                out.write(entry.getValue());
                out.write("\n");
            }
            out.flush();
            getLog().info(infoFileCreated ? "Created " : "Overwrote " + file.getAbsolutePath());
        } catch (IOException e) {
            getLog().warn(e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    private File getBuildDir() {
        if (StringUtils.isNotBlank(fileDir)) {
            getLog().info("Build Dir: " + fileDir);
            return new File(fileDir);
        } else {
            final String directory = project.getBuild().getDirectory();
            getLog().info("Build Dir: " + directory);
            return new File(directory);
        }
    }

    public List<String> getProjectProperties() {
        return projectProperties;
    }

    public List<String> getDeclaredProperties() {
        return declaredProperties;
    }

    public List<String> getSystemProperties() {
        return systemProperties;
    }

    public List<String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public String getDateTimePattern() {
        return dateTimePattern;
    }

    public boolean isIncludeVcsInfo() {
        return includeVcsInfo;
    }
}
