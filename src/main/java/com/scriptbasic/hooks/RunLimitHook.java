package com.scriptbasic.hooks;

import com.scriptbasic.api.Configuration;
import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.SimpleHook;

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
    private Integer stepLimit;
    private int currentSteps;
    private Long timeLimitMillis;
    private long scriptStartTime;

    private Optional<String> hookConfig(final String s) {
        return config.getConfigValue(configPrefix + s);
    }

    @Override
    public void initEx() {
        config = getInterpreter().getConfiguration();
        currentSteps = 0;
        scriptStartTime = System.currentTimeMillis();
        stepLimit = hookConfig("stepLimit").map(Integer::parseInt).orElse(null);
        timeLimitMillis = hookConfig("timeLimitMillis").map(Long::parseLong).orElse(null);
    }

    @Override
    public void beforeExecuteEx(final Command command) {
        if (stepLimit != null && currentSteps > stepLimit) {
            throw new RuntimeException("The code exceeded the maximum number of steps");
        }
        currentSteps++;

        if (timeLimitMillis != null && (System.currentTimeMillis() - scriptStartTime) > timeLimitMillis) {
            throw new RuntimeException(
                    "The code exceeded the maximum allowed time");
        }
    }
}
