package com.scriptbasic.memory;
import java.util.HashMap;
import java.util.Map;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
/**
 * @author Peter Verhas
 * date June 25, 2012
 *
 */
class BasicVariableRegistry extends BasicVariableMap {
    private enum VariableType {
        KNOWN_LOCAL, KNOWN_GLOBAL
    }
    private BasicVariableMap that;
    BasicVariableRegistry(BasicVariableMap that) {
        super(that);
        this.that = that;
    }
    private final Map<String, VariableType> registrationMap = new HashMap<>();
    /**
     * Assert that a variable is not registered to be local and global at the
     * same time. The implementation is lenient to allow double registration of
     * local and global but if ever a variable was registered to be local then
     * it has to remain that and the same holds for global.
     *
     * @param variableName
     * @param type
     *            either KNOWN_LOCAL or KNOWN_GLOBAL. The type that we intend
     *            the variable to be registered. So it should be KNOWN_LOCAL
     *            when the caller wants to register a variable as local.
     * @throws ExecutionException
     *             when the variable was already registered with a different
     *             type
     */
    private void assertNoOverregistration(String variableName, VariableType type)
            throws ExecutionException {
        that.assertCorrectCasing(variableName);
        String convertedVariableName = converted(variableName);
        if (registrationMap.containsKey(convertedVariableName)
                && registrationMap.get(convertedVariableName) != type) {
            throw new BasicRuntimeException("Variable '" + variableName
                    + "' can not be local and global at a time.");
        }
    }
    void registerLocal(String variableName) throws ExecutionException {
        assertNoOverregistration(variableName, VariableType.KNOWN_LOCAL);
        String convertedVariableName = converted(variableName);
        that.registerVariableCasing(variableName);
        registrationMap.put(convertedVariableName, VariableType.KNOWN_LOCAL);
    }
    void registerGlobal(String variableName) throws ExecutionException {
        assertNoOverregistration(variableName, VariableType.KNOWN_GLOBAL);
        String convertedVariableName = converted(variableName);
        that.registerVariableCasing(variableName);
        registrationMap.put(convertedVariableName, VariableType.KNOWN_GLOBAL);
    }
    boolean isGlobal(String variableName) throws ExecutionException {
        that.assertCorrectCasing(variableName);
        String convertedVariableName = converted(variableName);
        return registrationMap.get(convertedVariableName) == VariableType.KNOWN_GLOBAL;
    }
    boolean isLocal(String variableName) throws ExecutionException {
        that.assertCorrectCasing(variableName);
        String convertedVariableName = converted(variableName);
        return registrationMap.get(convertedVariableName) == VariableType.KNOWN_LOCAL;
    }
}