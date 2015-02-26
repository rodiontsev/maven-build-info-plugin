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

package com.rodiontsev.maven.plugins.buildinfo.utils;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Date: 25/02/15
 * Time: 22:45
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class InfoWriter {
    private static final String DEFAULT_VALUE = "";

    public void write(Map<String, String> info, @Nullable List<String> properties, PropertyMapper mapper) {
        if (properties != null) {
            for (String propertyName : properties) {
                String propertyValue = mapper.mapProperty(propertyName);
                info.put(propertyName, StringUtils.isNotEmpty(propertyValue) ? propertyValue : DEFAULT_VALUE);
            }
        }
    }

}
