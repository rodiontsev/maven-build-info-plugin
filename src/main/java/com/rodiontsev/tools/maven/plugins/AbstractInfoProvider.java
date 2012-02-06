package com.rodiontsev.tools.maven.plugins;

import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Date: 2/6/12
 * Time: 10:31 AM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public abstract class AbstractInfoProvider implements InfoProvider {

    private File lookupDirectory(MavenProject project, String child) throws FileNotFoundException {
        File dir;

        //Walk up the project parent hierarchy seeking the .hg directory
        MavenProject mavenProject = project;
        while (mavenProject != null) {
            dir = new File(mavenProject.getBasedir(), child);
            if (dir.exists() && dir.isDirectory()) {
                return dir;
            }
            // If we've reached the top-level parent and not found the .git directory, look one level further up
            if (mavenProject.getParent() == null && mavenProject.getBasedir() != null) {
                dir = new File(mavenProject.getBasedir().getParentFile(), child);
                if (dir.exists() && dir.isDirectory()) {
                    return dir;
                }
            }
            mavenProject = mavenProject.getParent();
        }

        throw new FileNotFoundException("Could not find " + child + " directory");
    }

    protected boolean isActive(MavenProject project, String child) {
        boolean result = false;
        try {
            File dir = lookupDirectory(project, child);
            result = (dir.exists() && dir.isDirectory()); //redundant check
        } catch (FileNotFoundException e) {
            //do nothing
        }
        return result;
    }

}
