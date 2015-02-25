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
import com.rodiontsev.maven.plugins.buildinfo.utils.InfoWriter;
import com.rodiontsev.maven.plugins.buildinfo.utils.PropertyMapper;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * InfoProvider for environment variables.
 *
 * @author <a href="https://github.com/shendrix">Steve Hendrix</a>
 */
public class EnvironmentVariablesProvider implements InfoProvider {

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        final Map<String, String> env = System.getenv();

        Map<String, String> info = new LinkedHashMap<String, String>();

        new InfoWriter().write(info, mojo.getEnvironmentVariables(), new PropertyMapper() {
            @Override
            public String mapProperty(String propertyName) {
                return env.get(propertyName);
            }
        });

        return info;
    }

}
