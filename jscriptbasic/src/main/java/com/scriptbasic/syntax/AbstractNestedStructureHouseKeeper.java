package com.scriptbasic.syntax;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;

public abstract class AbstractNestedStructureHouseKeeper implements
        NestedStructureHouseKeeper {
    private static final Logger log = LoggerFactory
            .getLogger(AbstractNestedStructureHouseKeeper.class);

    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

    @Override
    public void push(final NestedStructure element) {
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

    private boolean stackIsHealthy = true;

    protected boolean isStackIsHealthy() {
        return stackIsHealthy;
    }

    @Override
    public void push(Class<?> klass, NestedStructure element) {
        Structure stackFrame = new Structure();
        stackFrame.setElementType(klass);
        stackFrame.setPushedElement(element);
        stack.push(stackFrame);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NestedStructure> T pop(Class<T> expectedClass)
            throws AnalysisException {
        Structure stackFrame = stack.peek();
        if (!expectedClass.isAssignableFrom(stackFrame.getElementType())) {
            stackIsHealthy = false;
            SyntaxException se = new GenericSyntaxException(
                    "Bad nested structures");
            LexicalAnalyzer la = factory.get(LexicalAnalyzer.class);
            LexicalElement le;
            try {
                le = la.peek();
                se.setLocation(le);
            } catch (LexicalException e) {
                log.error(
                        "There was an error when trying to fetch the current source location",
                        e);
            }
            throw se;
        } else {
            stack.pop();
        }
        return (T) (stackFrame == null ? null : stackFrame.getPushedElement());
    }
}
