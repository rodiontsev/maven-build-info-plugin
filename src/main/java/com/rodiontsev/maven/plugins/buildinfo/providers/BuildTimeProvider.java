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
import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * BuildTimeProvider adds project's build date and time to the info file.
 *
 * Date: 25/02/15
 * Time: 22:22
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class BuildTimeProvider implements InfoProvider {

    @Override
    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        Map<String, String> info = new LinkedHashMap<String, String>();

        String dateTimePattern = mojo.getDateTimePattern();
        if (StringUtils.isNotBlank(dateTimePattern)) {
            String buildTime;
            try {
                DateFormat dateFormat = new SimpleDateFormat(dateTimePattern, Locale.ENGLISH);
                buildTime = dateFormat.format(new Date());
            } catch (IllegalArgumentException e) {
                buildTime = "the given pattern is invalid";
                mojo.getLog().warn(String.format("The given date-time pattern '%s' is invalid. Please read %s javadoc about user-defined patterns for date-time formatting.",
                        dateTimePattern, SimpleDateFormat.class.getCanonicalName()));
            }
            info.put("build.time", buildTime);
        }

        return info;
    }

}
