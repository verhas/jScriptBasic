package com.scriptbasic.executors;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
public class GenericExpressionList implements ExpressionList {
    private final List<Expression> expressionList = new LinkedList<Expression>();
    @Override
	public void add(final Expression expression) {
        this.expressionList.add(expression);
    }
    @Override
    public Iterator<Expression> iterator() {
        return this.expressionList.iterator();
    }
    /* (non-Javadoc)
     * @see com.scriptbasic.interfaces.GenericList#length()
     */
    @Override
    public int size() {
        return expressionList.size();
    }
}