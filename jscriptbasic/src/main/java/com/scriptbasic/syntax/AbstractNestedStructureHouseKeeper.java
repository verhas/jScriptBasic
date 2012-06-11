package com.scriptbasic.syntax;

import java.util.Stack;

import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.interfaces.SyntaxException;

public abstract class AbstractNestedStructureHouseKeeper implements
        NestedStructureHouseKeeper {

    @Override
    public void push(NestedStructure element) {
        push(element.getClass(), element);
    }

    protected static class Structure {
        private Class<?> elementType;
        private NestedStructure pushedElement;

        public Class<?> getElementType() {
            return elementType;
        }

        public void setElementType(Class<?> elementType) {
            this.elementType = elementType;
        }

        public NestedStructure getPushedElement() {
            return pushedElement;
        }

        public void setPushedElement(NestedStructure pushedElement) {
            this.pushedElement = pushedElement;
        }
    }

    private Stack<Structure> stack = new Stack<Structure>();

    @Override
    public void push(Class<?> klass, NestedStructure element) {
        Structure stackFrame = new Structure();
        stackFrame.setElementType(klass);
        stackFrame.setPushedElement(element);
        stack.push(stackFrame);
    }

    protected abstract Structure seekFrameError(Class<?> expectedClass);

    @Override
    public NestedStructure pop(Class<?> expectedClass) throws SyntaxException {
        Structure stackFrame = stack.peek();
        if (!expectedClass.equals(stackFrame.getElementType())) {
            stackFrame = seekFrameError(expectedClass);
        } else {
            stack.pop();
        }
        return stackFrame.getPushedElement();
    }

}
