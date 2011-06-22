package com.rodiontsev.tools.maven.plugins;

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
 * Date: 5/17/11
 * Time: 3:08 PM
 *
 * @author <a href="http://www.rodiontsev.com">Dmitry Rodiontsev</a>
 */
public class MercurialInfoProvider implements InfoProvider {

    private static final Pattern HG_OUTPUT_PATTERN = Pattern.compile("^(\\S+)\\s(\\S+)\\s(.+)$");

    public Map<String, String> getInfo(MavenProject project) {
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
        if ((result != null) && result.isSuccess()) {
            String output = result.getCommandOutput();
            if (output != null) {
                Matcher matcher  = HG_OUTPUT_PATTERN.matcher(output);
                if (matcher.find() && matcher.groupCount() == 3) {
                    StringBuilder changeset = new StringBuilder();
                    changeset.append("r").append(matcher.group(2)).append(":").append(matcher.group(1));
                    info.put("hg.changeset", changeset.toString());
                    info.put("hg.branch", matcher.group(3));
                }
            }
        }
        return info;
    }
}

class HgLogConsumer extends HgConsumer {
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
