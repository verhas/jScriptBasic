package com.scriptbasic.compiled.my;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.spi.Interpreter;

public class BasicCode {

    public void run(final Interpreter interpreter) throws ScriptBasicException {

        int _pc = 0; //program counter
        while (true) {
            switch (_pc) {
                case 0:
                    if ((66) > (53)) {
                        _pc++;
                    } else {
                        return;
                    }
                    break;
                case 1:
                    System.out.print(
                            ((BasicArrayValue) interpreter.getVariables().getVariableValue("ha")).get(((63) + ((54) * (18))) - (6000)));
                    _pc++;
                    break;
                case 2:
                    _pc = 0;
                    break;
                default:
                    return;
            }
        }
    }
}