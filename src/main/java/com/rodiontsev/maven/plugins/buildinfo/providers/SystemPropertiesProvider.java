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
 * InfoProvider for the system properties.
 *
 * Date: 2/1/13
 * Time: 5:06 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class SystemPropertiesProvider implements InfoProvider {
    private static final String DEFAULT_VALUE = "";

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();

        List<String> properties = mojo.getSystemProperties();
        if (properties != null) {
            for (String property : properties) {
                info.put(property, System.getProperty(property, DEFAULT_VALUE));
            }
        }

        return info;
    }

}
