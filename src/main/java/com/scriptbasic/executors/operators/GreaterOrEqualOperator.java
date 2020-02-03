package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;

public class GreaterOrEqualOperator extends AbstractCompareOperator {

    @Override
    protected Boolean compareTo(final double l, final double r)
            throws BasicRuntimeException {
        return l >= r;
    }

    @Override
    protected Boolean compareTo(final long l, final long r)
            throws BasicRuntimeException {
        return l >= r;
    }

    @Override
    protected Boolean compareTo(final String l, final String r)
            throws BasicRuntimeException {
        return l.compareTo(r) >= 0;
    }

    @Override
    protected Boolean compareTo(final Object l,
                                final Object r) throws BasicRuntimeException {
        return compareJavaObjectTo(l, r) >= 0;
    }

    @Override
    protected Boolean compareTo(final boolean l, final boolean r) {
        final var a = l ? 1 : 0;
        final var b = r ? 1 : 0;
        return a >= b;
    }

}
