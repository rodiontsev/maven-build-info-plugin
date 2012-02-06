package com.rodiontsev.tools.maven.plugins;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Date: 13.05.2011
 * Time: 12:00:00
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 *
 * @goal extract
 * @phase prepare-package
 */
public class BuildInfoMojo extends AbstractMojo {
    private static final String BUILD_INFO_FILE_NAME = "build.info";
    private static final String DEFAULT_VALUE = "";

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     */
    private List<String> systemProperties;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (InfoProvider provider : ServiceLoader.load(InfoProvider.class)) {
            if (provider.isActive(project)) {
                map.putAll(provider.getInfo(project));
            }
        }

        if (systemProperties != null) {
            for (String property : systemProperties) {
                map.put(property, System.getProperty(property, DEFAULT_VALUE));
            }
        }

        Build build = project.getBuild();
        StringBuilder filename = new StringBuilder();
        filename.append(build.getDirectory()).append(File.separator).append(BUILD_INFO_FILE_NAME);

        getLog().info("Writing to file " + filename.toString());

        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename.toString()), "UTF-8"));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                out.write(entry.getKey());
                out.write(" = ");
                out.write(entry.getValue());
                out.write("\n");
            }
            out.flush();
        } catch (IOException ioe) {
            getLog().warn(ioe.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}
