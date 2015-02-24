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
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class ProjectInfoProvider implements InfoProvider {

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {

        // finite set of project properties we expose
        final Map<String, String> props = new LinkedHashMap<String, String>(65);
        props.put("project.id",            project.getId());
        props.put("project.groupId",       project.getGroupId());
        props.put("project.artifactId",    project.getArtifactId());
        props.put("project.version",       project.getVersion());
        props.put("project.name",          project.getName());
        props.put("project.description",   project.getDescription());
        props.put("project.modelVersion",  project.getModelVersion());
        props.put("project.inceptionYear", project.getInceptionYear());
        props.put("project.packaging",     project.getPackaging());
        props.put("project.url",           project.getUrl());
        final MavenProject parent = project.getParent();
        if (parent != null) {
            props.put("project.parent.id",            parent.getId());
            props.put("project.parent.groupId",       parent.getGroupId());
            props.put("project.parent.artifactId",    parent.getArtifactId());
            props.put("project.parent.version",       parent.getVersion());
            props.put("project.parent.name",          parent.getName());
            props.put("project.parent.description",   parent.getDescription());
            props.put("project.parent.modelVersion",  parent.getModelVersion());
            props.put("project.parent.inceptionYear", parent.getInceptionYear());
            props.put("project.parent.packaging",     parent.getPackaging());
            props.put("project.parent.url",           parent.getUrl());
        }

        // properties the user wants
        Map<String, String> info = new LinkedHashMap<String, String>();

        if (mojo.getProjectProperties() != null) {
            for (String propertyName : mojo.getProjectProperties()) {
                String prop = props.get(propertyName);
                if (prop != null) {
                    info.put(propertyName, prop);
                }
            }
        }
        info.put("build.time", DateFormatUtils.format(new Date(), "d MMMM yyyy, HH:mm:ss ZZ", Locale.ENGLISH));

        return info;
    }

}
