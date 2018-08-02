package com.scriptbasic.hooks;

import com.scriptbasic.spi.SimpleHook;
import com.scriptbasic.spi.Command;
import com.scriptbasic.api.Configuration;

import java.util.Optional;

/**
 * This hook can be configured to limit the execution resources for a given
 * program. The limits are read from the configuration file.
 *
 * @author Peter Verhas
 * date Aug 4, 2012
 */
public class RunLimitHook extends SimpleHook {

    final private static String configPrefix = "RunLimitHook.";
    private Configuration config;
    private int stepLimit;
    private boolean stepIsLimited;
    private int currentSteps;
    private long timeLimitMillis;
    private boolean timeIsLimited;
    private long scriptStartTime;

    private Optional<String> hookConfig(final String s) {
        return config.getConfigValue(configPrefix + s);
    }

    @Override
    public void initEx() {
        config = getInterpreter().getConfiguration();
        currentSteps = 0;
        scriptStartTime = System.currentTimeMillis();
        final Optional<String> stepLimit = hookConfig("stepLimit");
        stepIsLimited = stepLimit.isPresent();
        if (stepIsLimited) {
            this.stepLimit = Integer.valueOf(stepLimit.get());
        }
        final Optional<String> timeLimitMillis = hookConfig("timeLimitMillis");
        timeIsLimited = timeLimitMillis.isPresent();
        if (timeIsLimited) {
            this.timeLimitMillis = Long.valueOf(timeLimitMillis.get());
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
            throw new RuntimeException(
                    "The code exceeded the maximum allowed time");
        }
    }
}
