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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * InfoProvider for environment variables.
 *
 * @author <a href="https://github.com/shendrix">Steve Hendrix</a>
 */
public class EnvironmentVariablesProvider implements InfoProvider {
    private static final String DEFAULT_VALUE = "";

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();

        List<String> properties = mojo.getEnvironmentVariables();
        if (properties != null) {
            Map<String, String> env = System.getenv();
            for (String property : properties) {
                String prop = env.get(property) != null ? env.get(property) : DEFAULT_VALUE;
                info.put(property, prop);
            }
        }

        return info;
    }

}
