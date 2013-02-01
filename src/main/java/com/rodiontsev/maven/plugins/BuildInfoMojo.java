package com.rodiontsev.maven.plugins;

import org.apache.commons.io.IOUtils;
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
    private static final String DEFAULT_BUILD_INFO_FILENAME = "build.info";

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

    /**
     * @parameter
     */
    private String filename;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (InfoProvider provider : ServiceLoader.load(InfoProvider.class)) {
            if (provider.isActive(project)) {
                map.putAll(provider.getInfo(project, this));
            }
        }

        String filename = project.getBuild().getDirectory()
                + File.separator + (this.filename != null ? this.filename : DEFAULT_BUILD_INFO_FILENAME);

        getLog().info("Writing to the file " + filename);

        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
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

    public List<String> getSystemProperties() {
        return systemProperties;
    }

}
