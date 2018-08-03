package com.scriptbasic;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 */
public class AbstractStringIOPojo extends AbstractStringBuilderIOPojo {
    public String getSStdin() {
        return super.getStdin() == null ? null : super.getStdin().toString();
    }

    /**
     * @param stdin the stdin to set
     */
    public void setSStdin(final String stdin) {
        super.setStdin(new StringBuilder(stdin));
    }

    public String getSStdout() {
        return super.getStdout() == null ? null : super.getStdout().toString();
    }

    /**
     * @param stdout the stdout to set
     */
    public void setSStdout(final String stdout) {
        super.setStdout(new StringBuilder(stdout));
    }

    public String getSStderr() {
        return super.getStderr() == null ? null : super.getStderr().toString();
    }

    /**
     * @param stderr the stderr to set
     */
    public void setSStderr(final String stderr) {
        super.setStderr(new StringBuilder(stderr));
    }
}
