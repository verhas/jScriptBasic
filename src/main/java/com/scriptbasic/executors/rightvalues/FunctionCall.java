package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.RightValueUtility;

import java.util.Iterator;
import java.util.List;

public class FunctionCall extends AbstractIdentifieredExpressionListedExpression {

    private boolean commandNeedLookup = true;
    private CommandSub commandSub = null;

    private static RightValue[] evaluateArguments(final ExpressionList argumentList,
                                                  final Interpreter interpreter) throws ScriptBasicException {
        final RightValue[] argumentValues;
        if (argumentList == null) {
            argumentValues = null;
        } else {
            final Iterator<Expression> expressionIterator = argumentList.iterator();
            argumentValues = new RightValue[argumentList.size()];
            for (int i = 0; i < argumentValues.length; i++) {
                argumentValues[i] = expressionIterator.next().evaluate(
                        interpreter);
            }
        }
        return argumentValues;
    }

    private RightValue callBasicFunction(final Interpreter interpreter)
            throws ScriptBasicException {
        return ExpressionUtility.callBasicFunction(interpreter,
                evaluateArguments(getExpressionList(), interpreter),
                commandSub, getVariableName());
    }

    private String toJavaBasicFunction(CompilerContext cc) {
        final var code = fragment();
        final var functionName = getVariableName().toLowerCase();
        final var method = cc.getInterpreter().getJavaMethod(null, functionName);
        code.add(method.getDeclaringClass().getName()+"."+method.getName());
        code.add("(");
        var sep = "";
        for( final var expression : getExpressionList()){
            code.add(sep + expression.toJava(cc));
            sep = ",";
        }
        code.add(")");
        return code.close();
    }

    private RightValue callJavaFunction(final Interpreter interpreter)
            throws ScriptBasicException {
        final RightValue result;
        final var functionName = getVariableName().toLowerCase();
        final List<RightValue> args = ExpressionUtility.evaluateExpressionList(
                interpreter, getExpressionList());
        final var method = interpreter.getJavaMethod(null, functionName);
        if (method == null) {
            throw new BasicRuntimeException("There is no callable function '"
                    + functionName + "'");
        } else {
            final Object methodResult;
            methodResult = ReflectionUtility.invoke(interpreter, functionName,
                    method, null, args);
            result = RightValueUtility.createRightValue(methodResult);
        }
        return result;
    }

    private void lookUpCommandSub(final Interpreter interpreter) {
        if (commandNeedLookup) {
            commandNeedLookup = false;
            commandSub = interpreter.getSubroutine(getVariableName());
        }
    }

    @Override
    public String toJava(CompilerContext cc){
        if( commandSub == null ){
            return toJavaJavaFunction(cc);
        }else{
            return toJavaBasicFunction(cc);

        }
    }

    private String toJavaJavaFunction(CompilerContext cc) {
        final var code = fragment();
        final var functionName = getVariableName().toLowerCase();
        final var method = cc.getInterpreter().getJavaMethod(null, functionName);
        code.add(method.getDeclaringClass().getName()+"."+method.getName());
        code.add("(");
        var sep = "";
        for( final var expression : getExpressionList()){
            code.add(sep + expression.toJava(cc));
            sep = ",";
        }
        code.add(")");
        return code.close();
    }



    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {

        lookUpCommandSub(interpreter);
        final RightValue result;
        if (commandSub == null) {
            result = callJavaFunction(interpreter);
        } else {
            result = callBasicFunction(interpreter);
        }
        return result;
    }
}
