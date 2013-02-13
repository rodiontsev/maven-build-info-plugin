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

package com.rodiontsev.maven.plugins.buildinfo.providers;

import com.rodiontsev.maven.plugins.buildinfo.BuildInfoMojo;
import com.rodiontsev.maven.plugins.buildinfo.InfoProvider;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;

/**
 * Date: 2/6/12
 * Time: 10:31 AM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public abstract class AbstractVcsInfoProvider implements InfoProvider {

    //Walk up the project parent hierarchy seeking the <child> directory
    private File lookup(File parent, String child) throws FileNotFoundException {
        if (parent == null) {
            throw new FileNotFoundException("Could not find " + child + " directory");
        }

        File dir = new File(parent, child);
        return (dir.exists() && dir.isDirectory()) ? dir : lookup(parent.getParentFile(), child);
    }

    protected boolean isDirectoryExists(MavenProject project, String child) {
        boolean result = false;
        try {
            File dir = lookup(project.getBasedir(), child);
            result = (dir.exists() && dir.isDirectory()); //redundant check
        } catch (FileNotFoundException e) {
            //do nothing
        }
        return result;
    }

    protected abstract boolean isActive(MavenProject project, BuildInfoMojo mojo);

    protected abstract Map<String, String> getScmInfo(MavenProject project, BuildInfoMojo mojo);

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        return mojo.isIncludeVcsInfo() && isActive(project, mojo) ?
                getScmInfo(project, mojo) : Collections.<String, String>emptyMap();
    }

}
