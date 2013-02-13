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
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.SvnExeScmProvider;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * InfoProvider for Subversion.
 *
 * Date: 1/28/13
 * Time: 5:37 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class SubversionInfoProvider extends AbstractVcsInfoProvider {
    private static final String DOT_SVN = ".svn";

    @Override
    protected boolean isActive(MavenProject project, BuildInfoMojo mojo) {
        return isDirectoryExists(project, DOT_SVN);
    }

    @Override
    protected Map<String, String> getScmInfo(MavenProject project, BuildInfoMojo mojo) {
        File basedir = project.getBasedir();

        ScmLogger logger = new DefaultLog();
        ScmFileSet fileSet = new ScmFileSet(basedir);

        SvnCommand infoCommand = new SvnExeScmProvider().getInfoCommand();
        infoCommand.setLogger(logger);

        InfoScmResult infoResult = null;
        try {
            ScmProviderRepository repository = new SvnScmProviderRepository(basedir.getAbsolutePath());
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
                    InfoItem item = items.get(0);
                    info.put("svn.url", item.getURL());
                    info.put("svn.revision", item.getRevision());
                    info.put("svn.last.changed.author", item.getLastChangedAuthor());
                    info.put("svn.last.changed.revision", item.getLastChangedRevision());
                    info.put("svn.last.changed.date", item.getLastChangedDate());
                }
            } else {
                info.put("svn.info.error", infoResult.getProviderMessage());
            }
        }

        return info;
    }

}
