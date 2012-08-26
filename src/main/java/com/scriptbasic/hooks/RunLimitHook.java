/**
 * 
 */
package com.scriptbasic.hooks;

import com.scriptbasic.executors.commands.CommandMethod;
import com.scriptbasic.executors.commands.CommandUse;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Configuration;

/**
 * This hook can be configured to limit the execution resources for a given
 * program. The limits are read from the configuration file.
 * 
 * @author Peter Verhas
 * date Aug 4, 2012
 * 
 */
public class RunLimitHook extends SimpleHook {

    private int stepLimit;
    private boolean stepIsLimited;
    private int currentSteps;
    private long timeLimitMillis;
    private boolean timeIsLimited;
    private long scriptStartTime;
    private boolean allowMethodRegistering = true;
    final private static String configPrefix = "RunLimitHook.";

    private static String X(String s) {
        return configPrefix + s;
    }

    @Override
    public void initEx() {
        currentSteps = 0;
        scriptStartTime = System.currentTimeMillis();
        Configuration config = getInterpreter().getConfiguration();
        String stepLimitString = config.getConfigValue(X("stepLimit"));
        stepIsLimited = stepLimitString != null;
        if (stepIsLimited) {
            this.stepLimit = Integer.valueOf(stepLimitString);
        }
        String timeLimitMillisString = config
                .getConfigValue(X("timeLimitMillis"));
        timeIsLimited = timeLimitMillisString != null;
        if (timeIsLimited) {
            this.timeLimitMillis = Long.valueOf(timeLimitMillisString);
            this.scriptStartTime = System.currentTimeMillis();
        }
        allowMethodRegistering = Boolean.valueOf(config.getConfigValue(
                X("allowJavaMethods"), "true"));
    }

    @Override
    public void beforeExecuteEx(final Command command) {
        if (stepIsLimited) {
            currentSteps++;
            if (currentSteps > stepLimit) {
                throw new RuntimeException(
                        "The code exceeded the maximum number of steps");
            }
            if (!allowMethodRegistering
                    && (command instanceof CommandMethod || command instanceof CommandUse)) {
                throw new RuntimeException(
                        "Registering Java methods is forbidden in the configuration");
            }
        }
        if (timeIsLimited
                && (System.currentTimeMillis() - scriptStartTime) > timeLimitMillis) {
            throw new RuntimeException(
                    "The code exceeded the maximum allowed time");
        }
    }
}
