package com.rodiontsev.tools.maven.plugins;

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
 * Date: 2/6/12
 * Time: 4:58 AM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class GitInfoProvider extends AbstractInfoProvider {
    private static final String DOT_GIT = ".git";

    public boolean isActive(MavenProject project) {
        return isActive(project, DOT_GIT);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getInfo(MavenProject project) {
        File basedir = project.getBasedir();

        InfoScmResult result = null;

        ScmLogger logger = new DefaultLog();

        GitCommand command = new GitExeScmProvider().getInfoCommand();
        command.setLogger(logger);
        try {
            ScmProviderRepository repository = new GitScmProviderRepository(basedir.getAbsolutePath());
            ScmFileSet fileSet = new ScmFileSet(basedir);
            CommandParameters parameters = new CommandParameters();
            result = (InfoScmResult) command.execute(repository, fileSet, parameters);
        } catch (ScmException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        Map<String, String> info = new LinkedHashMap<String, String>();
        if (result != null) {
            if (result.isSuccess()) {
                List<InfoItem> items = result.getInfoItems();
                if ((items != null) && (items.size() == 1)) {
                    info.put("git.revision", items.get(0).getRevision());
                } else {
                    info.put("git.error", "The command returned incorrect number of arguments");
                }
            } else {
                info.put("git.error", result.getProviderMessage());
            }

        }
        return info;
    }

}
