/**
 * 
 */
package com.scriptbasic;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 * 
 */
public class AbstractStringIOPojo extends AbstractStringBuilderIOPojo {
    /**
     * @param stdin
     *            the stdin to set
     */
    public void setSStdin(String stdin) {
        super.setStdin(new StringBuilder(stdin));
    }

    /**
     * @param stdout
     *            the stdout to set
     */
    public void setSStdout(String stdout) {
        super.setStdout(new StringBuilder(stdout));
    }

    /**
     * @param stderr
     *            the stderr to set
     */
    public void setSStderr(String stderr) {
        super.setStderr(new StringBuilder(stderr));
    }

    public String getSStdin() {
        return super.getStdin() == null ? null : super.getStdin().toString();
    }

    public String getSStdout() {
        return super.getStdout() == null ? null : super.getStdout().toString();
    }

    public String getSStderr() {
        return super.getStderr() == null ? null : super.getStderr().toString();
    }
}
