package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.utility.SyntaxExceptionUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class AbstractNestedStructureHouseKeeper implements NestedStructureHouseKeeper {
    private static final Logger LOG = LoggerFactory.getLogger();
    private static final Structure MATCH_NOTHING = new Structure() {
        public <T> boolean match(final Class<T> expectedClass) {
            return false;
        }
    };
    private final Stack<Structure> stack = new Stack<>();
    private final LexicalAnalyzer analyzer;
    private boolean stackIsHealthy = true;
    private final Stack<EndOfStatementProcessor> endOfStatementProcessors = new Stack<>();

    protected AbstractNestedStructureHouseKeeper(final LexicalAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public void push(final NestedStructure element) {
        push(element.getClass(), element);
    }

    protected boolean isStackIsHealthy() {
        return stackIsHealthy;
    }

    @Override
    public void push(final Class<?> klass, final NestedStructure element) {
        final var stackFrame = new Structure();
        stackFrame.setElementType(klass);
        stackFrame.setPushedElement(element);
        stack.push(stackFrame);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NestedStructure> T pop(final Class<T> expectedClass)
            throws AnalysisException {
        final var stackFrame = stack.isEmpty() ? MATCH_NOTHING : stack.peek();
        if (!stackFrame.match(expectedClass)) {
            stackIsHealthy = false;
            final var se = new BasicSyntaxException("Bad nested structures");
            try {
                se.setLocation(analyzer.peek());
            } catch (final LexicalException e) {
                LOG.error("There was an error when trying to fetch the current source location", e);
            }
            throw se;
        } else {
            stack.pop();
        }
        return (T) stackFrame.getPushedElement();
    }

    protected static class Structure {
        private Class<?> elementType;
        private NestedStructure pushedElement;

        public Class<?> getElementType() {
            return elementType;
        }

        public void setElementType(final Class<?> elementType) {
            this.elementType = elementType;
        }

        public NestedStructure getPushedElement() {
            return pushedElement;
        }

        public void setPushedElement(final NestedStructure pushedElement) {
            this.pushedElement = pushedElement;
        }

        public <T> boolean match(final Class<T> expectedClass) {
            return expectedClass.isAssignableFrom(getElementType());
        }
    }
    
    @Override
    public void checkFinalState() throws AnalysisException {
        if (stack.size() > 0) {
            throw new BasicSyntaxException("There is at least one opened block on the stack. Block is not properly closed.");
        }
        if (endOfStatementProcessors.size() > 0) {
            throw new BasicSyntaxException("There is at least one unfinished statement.");
        }
    }
    
    @Override
    public void consumeEndOfStatement() throws AnalysisException {
 
        final var numOfProcessors = endOfStatementProcessors.size();
        if(numOfProcessors>0) {
            // Copy processors and revert order
            // Note: Processors might be unregistered while iterating them
            List<EndOfStatementProcessor> processors = new ArrayList<>(numOfProcessors);
            final var iter = endOfStatementProcessors.listIterator(endOfStatementProcessors.size());
            while(iter.hasPrevious()) {
                processors.add(iter.previous());
            }
            // Run processors
            for(final var processor: processors) {
                final var result = processor.consumeEndOfStatement();
                if(result==EndOfStatementResult.CONSUMED) {
                    return;
                }
            }
        }
        
        final var le = analyzer.get();
        if (le != null && !(le.isLineTerminator() || le.isStatementSeparator())) {
            SyntaxExceptionUtility.throwSyntaxException(
                    "There are extra characters following the expression.", le);
        }
    }
    
    @Override
    public void pushEndOfStatementProcessor(EndOfStatementProcessor endOfStatementProcessor)
    {
        endOfStatementProcessors.push(endOfStatementProcessor);
    }
    
    @Override
    public EndOfStatementProcessor popEndOfStatementProcessor() {
        return endOfStatementProcessors.pop();
    }
}
