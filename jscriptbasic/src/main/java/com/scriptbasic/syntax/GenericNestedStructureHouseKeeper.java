package com.scriptbasic.syntax;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.NestedStructure;

/**
 * This version just does not nothing special to recover from error.
 * 
 * @author Peter Verhas
 * @date June 11, 2012
 */
public class GenericNestedStructureHouseKeeper extends
        AbstractNestedStructureHouseKeeper {
    @Override
    public <T extends NestedStructure> T pop(
            Class<T> expectedClass) throws SyntaxException {
        if (isStackIsHealthy()) {
            return super.pop(expectedClass);
        } else {
            throw new BasicInterpreterInternalError(
                    "No nested structure class error recovery is implemented");
        }
    }
}
