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
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * InfoProvider for Git.
 *
 * Date: 2/6/12
 * Time: 4:58 AM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class GitInfoProvider extends AbstractVcsInfoProvider {
    private static final String DOT_GIT = ".git";

    @Override
    protected boolean isActive(MavenProject project, BuildInfoMojo mojo) {
        return isDirectoryExists(project, DOT_GIT);
    }

    @Override
    protected Map<String, String> getScmInfo(MavenProject project, BuildInfoMojo mojo) {
        File basedir = project.getBasedir();

        ScmLogger logger = new DefaultLog();
        ScmFileSet fileSet = new ScmFileSet(basedir);

        GitCommand infoCommand = new GitExeScmProvider().getInfoCommand();
        infoCommand.setLogger(logger);

        InfoScmResult infoResult = null;
        try {
            ScmProviderRepository repository = new GitScmProviderRepository(basedir.getAbsolutePath());
            CommandParameters parameters = new CommandParameters();

            infoResult = (InfoScmResult) infoCommand.execute(repository, fileSet, parameters);
        } catch (ScmException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        Map<String, String> info = new LinkedHashMap<String, String>();

        if (infoResult != null) {
            if (infoResult.isSuccess()) {
                List<InfoItem> items = infoResult.getInfoItems();
                if ((items != null) && (items.size() == 1)) {
                    info.put("git.revision", items.get(0).getRevision());
                } else {
                    info.put("git.error", "The command returned incorrect number of arguments");
                }
            } else {
                info.put("git.error", infoResult.getProviderMessage());
            }
        }

        return info;
    }

}
