package com.scriptbasic.executors.leftvalues;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.VariableMap;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.LeftValue;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.RightValueUtility;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Verhas
 * date June 13, 2012
 */
public class BasicLeftValue implements LeftValue {
    final private static Logger LOG = LoggerFactory.getLogger();
    private final List<LeftValueModifier> modifiers = new LinkedList<>();
    /**
     * The identifier that is the name of the local or global variable.
     */
    private String identifier;

    /**
     * Handle field access modifier.
     * <p>
     * When the BASIC program has an assignment of the form:
     * <p>
     * <pre>
     * a[i].blabla.z[13,2] = 5
     * </pre>
     * <p>
     * then the {@code a[i].blabla.z} has to be evaluated as "variable" and then
     * the "variable"{@code [13,2]} should be used as store location to save the
     * right value to (in the example the long number 5).
     * <p>
     * jSB terminology calls the {@code [i].blabla.z[13,2]} part as modifiers. The
     * {@code [i]} an an array index modifier, {@code .blabla} is field access modifier and so on.
     * This terminology is a bit unfortunate colliding with the Java class member access modifiers.
     * <p>
     * If the list of modifiers has next element following the current one (
     * {@code hasNext} is {@code true}), then this is not the last modifier and
     * in this case the next {@code variable} is to be returned.
     * <p>
     * If the list of modifiers does not have next element ({@code hasNext} is
     * {@code false}) then this is the last modifier and it has to be used to
     * store the value {@code rightValue} in the variable+modifier location.
     *
     * @param variable   the variable from where the algorithm goes on or where the
     *                   value is to be stored
     * @param modifier   the actual modifier
     * @param hasNext    {@code true} if this is not the last modifier
     * @param rightValue the value to store when this is the last modifier
     * @return the next variable or {@code null} if the value was stored
     * @throws ScriptBasicException in case of exception
     */
    private static RightValue handleAccessModifier(RightValue variable,
                                                   final LeftValueModifier modifier,
                                                   final boolean hasNext,
                                                   final RightValue rightValue,
                                                   final Interpreter interpreter)
            throws ScriptBasicException {
        if (modifier instanceof ArrayElementAccessLeftValueModifier) {
            variable = handleArrayElementAccess(variable,
                    (ArrayElementAccessLeftValueModifier) modifier, hasNext,
                    rightValue, interpreter);
        } else if (modifier instanceof ObjectFieldAccessLeftValueModifier) {
            variable = handleObjectFieldAccess(variable,
                    (ObjectFieldAccessLeftValueModifier) modifier, hasNext,
                    rightValue);
        }
        return variable;
    }

    /**
     * Handle variable access modifier in case when the modifier is object field
     * access. {@link #handleAccessModifier(RightValue, LeftValueModifier, boolean, RightValue, Interpreter)}.
     *
     * @param variable   parameter
     * @param modifier   parameter
     * @param hasNext    parameter
     * @param rightValue parameter
     * @return return value
     * @throws BasicRuntimeException in case of exception
     */
    private static RightValue handleObjectFieldAccess(final RightValue variable,
                                                      final ObjectFieldAccessLeftValueModifier modifier,
                                                      final boolean hasNext,
                                                      final RightValue rightValue)
            throws ScriptBasicException {
        final var fieldName = modifier.getFieldName();
        if (!(variable instanceof BasicJavaObjectValue)) {
            throw new BasicRuntimeException(variable
                    + " is not an object, can not access its field '"
                    + fieldName + "'");
        }
        final var bjov = (BasicJavaObjectValue) variable;
        final var object = bjov.getValue();
        if (hasNext) {
            final var fieldObject = KlassUtility.getField(object, fieldName);
            return RightValueUtility.createRightValue(fieldObject);
        } else {
            final var valueObject = RightValueUtility.getValueObject(rightValue);
            KlassUtility.setField(object, fieldName, valueObject);
            return null;
        }
    }

    /**
     * Handle variable access modifier in case when the modifier is array
     * element access. {@link #handleAccessModifier(RightValue, LeftValueModifier, boolean,
     * RightValue, Interpreter)}.
     *
     * @param variable    parameter
     * @param modifier    parameter
     * @param hasNext     parameter
     * @param rightValue  parameter
     * @param interpreter is used to evaluate the expression that stands between the {@code [} and {@code ]}
     *                    characters. Note that this is not needed when a field access is evaluated.
     * @return return value
     * @throws ScriptBasicException in case of exception
     */
    private static RightValue handleArrayElementAccess(RightValue variable,
                                                       final ArrayElementAccessLeftValueModifier modifier,
                                                       final boolean hasNext,
                                                       final RightValue rightValue,
                                                       final Interpreter interpreter)
            throws ScriptBasicException {
        final Iterator<Expression> expressionIterator = modifier.getIndexList()
                .iterator();
        while (expressionIterator.hasNext()) {
            final var expression = expressionIterator.next();
            final var index = expression.evaluate(interpreter);

            if (variable instanceof BasicArray) {

                variable = handleBasicArrayElementAccess(
                        (BasicArray) variable,
                        RightValueUtility.convert2Integer(index),
                        (hasNext || expressionIterator.hasNext()), rightValue,
                        interpreter);

            } else {
                throw new BasicRuntimeException(variable
                        + " is not an array, can not access its index");
            }
        }
        return variable;
    }

    private static RightValue handleBasicArrayElementAccess(
            final BasicArray variable, final Integer index, final boolean hasNext,
            final RightValue rightValue, final Interpreter interpreter)
            throws ScriptBasicException {
        if (hasNext) {
            final RightValue arrayElement;
            final var object = variable.get(index);
            if (object instanceof RightValue) {
                arrayElement = (RightValue) object;
            } else {
                arrayElement = new BasicArrayValue(interpreter);
                variable.set(index, arrayElement);
            }
            return arrayElement;
        } else {
            variable.set(index, rightValue);
            return null;
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public List<LeftValueModifier> getModifiers() {
        return modifiers;
    }

    public boolean hasModifiers() {
        return modifiers.size() > 0;
    }

    public void addModifier(final LeftValueModifier modifier) {
        modifiers.add(modifier);
    }

    @Override
    public void setValue(final RightValue rightValue, final Interpreter interpreter)
            throws ScriptBasicException {
        final VariableMap variableMap = interpreter.getVariables();
        if (modifiers.isEmpty()) {
            LOG.debug("setting the variable '{}'", getIdentifier());
            variableMap.setVariable(getIdentifier(), rightValue);
        } else {
            RightValue variableCurrentValue = variableMap.getVariableValue(getIdentifier());
            variableCurrentValue = emptyArrayIfUnderOrElseSelf(variableCurrentValue, interpreter);
            handleAllAccessModifier(rightValue, interpreter, variableCurrentValue);
        }
    }

    private void handleAllAccessModifier(final RightValue rightValue, final Interpreter interpreter,
                                         RightValue variableCurrentValue) throws ScriptBasicException {
        final Iterator<LeftValueModifier> modifierIterator = modifiers.iterator();
        do {
            variableCurrentValue = handleAccessModifier(variableCurrentValue,
                    modifierIterator.next(), modifierIterator.hasNext(),
                    rightValue, interpreter);

        } while (variableCurrentValue != null);
    }

    /**
     * Return the right value itself if it is not null (undefined) or
     * a newly allocated {@link BasicArray} that will be used for
     * further indexing to store the final right value.
     * <p>
     * This method is invoked when a left value contains some modifier
     * and the value that starts the left value is undefined. That way
     * {@code var[i] = xxx} type of assignments work fine even if the {@code var} was
     * not used before, because arrays are allocated automatically.
     *
     * @param value       that is the current value of the
     * @param interpreter used to allocate the new array
     * @return the value or a newly allocated array, which is also stored in the current left value left identifier
     * @throws ScriptBasicException in case of exception
     */
    private RightValue emptyArrayIfUnderOrElseSelf(final RightValue value,
                                                   final Interpreter interpreter)
            throws ScriptBasicException {
        final RightValue newValue;
        if (value == null) {
            newValue = new BasicArrayValue(interpreter);
            interpreter.getVariables().setVariable(getIdentifier(), newValue);
        } else {
            newValue = value;
        }
        return newValue;
    }

}
