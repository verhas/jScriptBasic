package com.scriptbasic.context;

import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.Interpreter;

import java.util.HashMap;
import java.util.Map;

public class CompilerContext {
    final public Map<Command, Integer> nr = new HashMap<>();

    final Map<String, Integer> variableSerial = new HashMap<>();

    private Interpreter interpreter;

    public int serial(final String name) {
        if (!variableSerial.containsKey(name)) {
            variableSerial.put(name, variableSerial.size());
        }
        return variableSerial.get(name);
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(final Interpreter interpreter) {
        this.interpreter = interpreter;
    }
}
