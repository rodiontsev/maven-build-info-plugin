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
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgCommandConstants;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfoProvider for Mercurial.
 *
 * Date: 5/17/11
 * Time: 3:08 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class MercurialInfoProvider extends AbstractVcsInfoProvider {

    private class HgLogConsumer extends HgConsumer {
        private final StringBuilder out = new StringBuilder();

        public HgLogConsumer(ScmLogger logger) {
            super(logger);
        }

        @Override
        public void consumeLine(String line) {
            out.append(line).append("\n");
        }

        @Override
        public String getStdErr() {
            return out.toString();
        }
    }

    private static final String DOT_HG = ".hg";
    private static final Pattern HG_OUTPUT_PATTERN = Pattern.compile("^(\\S+)\\s(\\S+)\\s(.+)$");

    @Override
    protected boolean isActive(MavenProject project, BuildInfoMojo mojo) {
        return isDirectoryExists(project, DOT_HG);
    }

    @Override
    protected Map<String, String> getScmInfo(MavenProject project, BuildInfoMojo mojo) {
        ScmLogger logger = new DefaultLog();
        HgLogConsumer consumer = new HgLogConsumer(logger);
        ScmResult result = null;
        try {
            result = HgUtils.execute(
                    consumer,
                    logger,
                    project.getBasedir(),
                    new String[]{HgCommandConstants.REVNO_CMD, "-n", "-i", "-b"});
        } catch (ScmException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        Map<String, String> info = new LinkedHashMap<String, String>();
        if (result != null) {
            if (result.isSuccess()) {
                String output = result.getCommandOutput();
                if (output != null) {
                    Matcher matcher  = HG_OUTPUT_PATTERN.matcher(output);
                    if (matcher.find() && matcher.groupCount() == 3) {
                        StringBuilder changeset = new StringBuilder();
                        changeset.append("r").append(matcher.group(2)).append(":").append(matcher.group(1));
                        info.put("hg.changeset", changeset.toString());
                        info.put("hg.branch", matcher.group(3));
                    } else {
                        info.put("hg.error", "The command returned incorrect number of arguments");
                    }
                }
            } else {
                info.put("hg.error", result.getProviderMessage());
            }
        }
        return info;
    }
}
