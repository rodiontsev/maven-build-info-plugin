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
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.maven.project.MavenProject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Default InfoProvider.
 *
 * Date: 6/22/11
 * Time: 11:21 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>,
 * @author <a href="https://github.com/vlfig">Vasco Figueira</a>
 * @author <a href="https://github.com/shendrix">Steve Hendrix</a>
 */
public class ProjectInfoProvider implements InfoProvider {

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();

        if (mojo.getProjectProperties() != null) {
            // finite set of project properties we expose
            final Map<String, String> projectProperties = new LinkedHashMap<String, String>(65);
            projectProperties.put("project.id", project.getId());
            projectProperties.put("project.groupId", project.getGroupId());
            projectProperties.put("project.artifactId", project.getArtifactId());
            projectProperties.put("project.version", project.getVersion());
            projectProperties.put("project.name", project.getName());
            projectProperties.put("project.description", project.getDescription());
            projectProperties.put("project.modelVersion", project.getModelVersion());
            projectProperties.put("project.inceptionYear", project.getInceptionYear());
            projectProperties.put("project.packaging", project.getPackaging());
            projectProperties.put("project.url", project.getUrl());

            final MavenProject parent = project.getParent();
            if (parent != null) {
                projectProperties.put("project.parent.id", parent.getId());
                projectProperties.put("project.parent.groupId", parent.getGroupId());
                projectProperties.put("project.parent.artifactId", parent.getArtifactId());
                projectProperties.put("project.parent.version", parent.getVersion());
                projectProperties.put("project.parent.name", parent.getName());
                projectProperties.put("project.parent.description", parent.getDescription());
                projectProperties.put("project.parent.modelVersion", parent.getModelVersion());
                projectProperties.put("project.parent.inceptionYear", parent.getInceptionYear());
                projectProperties.put("project.parent.packaging", parent.getPackaging());
                projectProperties.put("project.parent.url", parent.getUrl());
            }

            for (String propertyName : mojo.getProjectProperties()) {
                String propertyValue = projectProperties.get(propertyName);
                if (propertyValue != null) {
                    info.put(propertyName, propertyValue);
                }
            }
        }

        info.put("build.time", DateFormatUtils.format(new Date(), "d MMMM yyyy, HH:mm:ss ZZ", Locale.ENGLISH));

        return info;
    }

}
