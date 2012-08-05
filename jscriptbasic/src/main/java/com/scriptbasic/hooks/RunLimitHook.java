/**
 * 
 */
package com.scriptbasic.hooks;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Configuration;

/**
 * This hook can be configured to limit the execution resources for a given
 * program. The limits are read from the configuration file.
 * 
 * @author Peter Verhas
 * @date Aug 4, 2012
 * 
 */
public class RunLimitHook extends SimpleHook {

    private int stepLimit;
    private boolean stepIsLimited;
    private int currentSteps;
    private long timeLimitMillis;
    private boolean timeIsLimited;
    private long scriptStartTime;

    @Override
    public void initEx() {
        currentSteps = 0;
        scriptStartTime = System.currentTimeMillis();
        Configuration config = getInterpreter().getConfiguration();
        String stepLimitString = config.getConfigValue("RunLimitHook.stepLimit");
        stepIsLimited = stepLimitString != null;
        if (stepIsLimited) {
            this.stepLimit = Integer.valueOf(stepLimitString);
        }
        String timeLimitMillisString = config
                .getConfigValue("RunLimitHook.timeLimitMillis");
        timeIsLimited = timeLimitMillisString != null;
        if (timeIsLimited) {
            this.timeLimitMillis = Long.valueOf(timeLimitMillisString);
            this.scriptStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public void beforeExecuteEx(final Command command) {
        if (stepIsLimited) {
            currentSteps++;
            if (currentSteps > stepLimit) {
                throw new RuntimeException(
                        "The code exceeded the maximum number of steps");
            }
        }
        if (timeIsLimited
                && (System.currentTimeMillis() - scriptStartTime) > timeLimitMillis) {
            throw new RuntimeException("The code exceeded the maximum allowed time");
        }
    }

}
