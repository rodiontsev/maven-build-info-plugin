package com.rodiontsev.maven.plugins.providers;

import com.rodiontsev.maven.plugins.BuildInfoMojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
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
 * Date: 28.01.13
 * Time: 17:37
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class SubversionInfoProvider extends AbstractInfoProvider {
    private static final String DOT_SVN = ".svn";

    public boolean isActive(MavenProject project) {
        return isActive(project, DOT_SVN);
    }

    public Map<String, String> getInfo(MavenProject project, BuildInfoMojo mojo) {
        ScmLogger logger = new DefaultLog();

        File basedir = project.getBasedir();
        ScmFileSet fileSet = new ScmFileSet(basedir);

        ScmProviderRepository repository = new SvnScmProviderRepository(basedir.getAbsolutePath());
        CommandParameters parameters = new CommandParameters();

        SvnCommand infoCommand = new SvnExeScmProvider().getInfoCommand();
        infoCommand.setLogger(logger);

        InfoScmResult infoResult = null;
        try {
            infoResult = (InfoScmResult) infoCommand.execute(repository, fileSet, parameters);
        } catch (ScmException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        StatusScmResult statusResult = null;
        try {
            statusResult = new SvnExeScmProvider().status(repository, fileSet, parameters);
        } catch (ScmException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
        }

        Map<String, String> info = new LinkedHashMap<String, String>();

        if (infoResult != null) {
            if (infoResult.isSuccess()) {
                List<InfoItem> items = infoResult.getInfoItems();
                for (InfoItem item : items) {
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

        if (statusResult != null) {
            if (statusResult.isSuccess()) {
                List<ScmFile> files = statusResult.getChangedFiles();
                if (!files.isEmpty()) {
                    info.put("# The status of working copy files and directories", "");

                    for (ScmFile file : files) {
                        info.put("# " + file.getPath(), file.getStatus().toString());
                    }
                }
            } else {
                info.put("svn.status.error", statusResult.getProviderMessage());
            }
        }

        return info;
    }

}
