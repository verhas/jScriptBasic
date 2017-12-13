package com.scriptbasic.spi;

import java.util.Set;

/**
 * Provide a set of classes.
 * <p>
 * Modules that contain classes defining methods annotated with {@link com.scriptbasic.api.BasicFunction}
 * should provide this interface as service so that the {@link java.util.ServiceLoader} can find them.
 * ScriptBasic registers the classes provided by the method {@link #provide()} when it also registers the
 * methods that are implemented in this module.
 */
public interface ClassSetProvider {

    /**
     * @return the set of classes that are interesting for the caller.
     */
    Set<Class<?>> provide();
}
