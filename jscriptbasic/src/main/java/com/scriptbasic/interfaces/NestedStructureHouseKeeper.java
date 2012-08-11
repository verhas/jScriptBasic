package com.scriptbasic.interfaces;
/**
 * Object implementing this interface keep track of the programming structures
 * that can be nested into each other. For example loops, functions and
 * procedures and so on.
 *
 * @author Peter Verhas
 * date June 8, 2012
 */
public interface NestedStructureHouseKeeper extends FactoryManaged {
    /**
     * Push a nested structure object on the housekeeping stack.
     *
     * @param element
     *            the element to be stored on the stack.
     * @param klass
     *            is the class that we will expect when we pop this element off
     */
    void push(Class<?> klass, NestedStructure element);
    /**
     * Push a nested structure object on the housekeeping stack. This version of
     * push does push the object associated with the actual class of the object.
     *
     * @param element
     *            to push on the stack.
     */
    void push(NestedStructure element);
    /**
     * Pops one element from the stack.
     * <p>
     * Note that nested element have to be pushed on the stack nested. If we get
     * an element off the stack that was not expected it means syntax error and
     * therefore in this case exception is thrown.
     * <p>
     * When exception was thrown the functioning of the object is not defined by
     * this interface. Some implementation may seek the appropriate element in
     * the stack and throw off all top element until a proper type of element is
     * found assuming that the user forgot to close some internal programming
     * structured in the scripted language. Other implementations may follow
     * more complex strategy to recover from such an error. In either case the
     * sole reason of further syntax analysis is to discover as many syntax
     * error as possible following the first one.
     *
     * @param <T>
     *            expected type of the element
     * @return the top element
     * @throws AnalysisException
     *             when the top element of the stack is not the type that we
     *             expect
     */
    <T extends NestedStructure> T pop(Class<T> expectedClass)
            throws AnalysisException;
}