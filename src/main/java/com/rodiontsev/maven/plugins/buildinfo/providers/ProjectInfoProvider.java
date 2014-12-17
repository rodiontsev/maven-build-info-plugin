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
        Map<String, String> info = new LinkedHashMap<String, String>();
        info.put("project.id",            project.getId());
        info.put("project.groupId",       project.getGroupId());
        info.put("project.artifactId",    project.getArtifactId());
        info.put("project.version",       project.getVersion());
        info.put("project.name",          project.getName());
        info.put("project.description",   project.getDescription());
        info.put("project.modelVersion",  project.getModelVersion());
        info.put("project.inceptionYear", project.getInceptionYear());
        info.put("project.packaging",     project.getPackaging());
        info.put("project.url",           project.getUrl());
        final MavenProject parent = project.getParent();
        if (parent != null) {
            info.put("project.parent.id",            parent.getId());
            info.put("project.parent.groupId",       parent.getGroupId());
            info.put("project.parent.artifactId",    parent.getArtifactId());
            info.put("project.parent.version",       parent.getVersion());
            info.put("project.parent.name",          parent.getName());
            info.put("project.parent.description",   parent.getDescription());
            info.put("project.parent.modelVersion",  parent.getModelVersion());
            info.put("project.parent.inceptionYear", parent.getInceptionYear());
            info.put("project.parent.packaging",     parent.getPackaging());
            info.put("project.parent.url",           parent.getUrl());
        }
        info.put("build.time", DateFormatUtils.format(new Date(), "d MMMM yyyy, HH:mm:ss ZZ", Locale.ENGLISH));
        return info;
    }

}
