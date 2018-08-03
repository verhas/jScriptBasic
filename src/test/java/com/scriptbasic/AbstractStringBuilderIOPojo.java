package com.scriptbasic;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 */
public abstract class AbstractStringBuilderIOPojo {
    private StringBuilder stdin;
    private StringBuilder stdout;
    private StringBuilder stderr;

    /**
     * @return the stdin
     */
    public StringBuilder getStdin() {
        return stdin;
    }

    /**
     * @param stdin the stdin to set
     */
    public void setStdin(final StringBuilder stdin) {
        this.stdin = stdin;
    }

    /**
     * @return the stdout
     */
    public StringBuilder getStdout() {
        return stdout;
    }

    /**
     * @param stdout the stdout to set
     */
    public void setStdout(final StringBuilder stdout) {
        this.stdout = stdout;
    }

    /**
     * @return the stderr
     */
    public StringBuilder getStderr() {
        return stderr;
    }

    /**
     * @param stderr the stderr to set
     */
    public void setStderr(final StringBuilder stderr) {
        this.stderr = stderr;
    }

}
