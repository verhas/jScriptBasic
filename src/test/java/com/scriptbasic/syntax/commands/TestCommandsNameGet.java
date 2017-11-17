package com.scriptbasic.syntax.commands;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

public class TestCommandsNameGet {

    @Test
    public void testCommandGetName() throws InstantiationException,
            IllegalAccessException {
        List<Class<? extends AbstractCommandAnalyzer>> classes = new LinkedList<Class<? extends AbstractCommandAnalyzer>>();
        classes.add(CommandAnalyzerCall.class);
        classes.add(CommandAnalyzerElse.class);
        classes.add(CommandAnalyzerElseIf.class);
        classes.add(CommandAnalyzerEndIf.class);
        classes.add(CommandAnalyzerEndSub.class);
        classes.add(CommandAnalyzerFor.class);
        classes.add(CommandAnalyzerGlobal.class);
        classes.add(CommandAnalyzerIf.class);
        classes.add(CommandAnalyzerLet.class);
        classes.add(CommandAnalyzerLocal.class);
        classes.add(CommandAnalyzerMethod.class);
        classes.add(CommandAnalyzerNext.class);
        classes.add(CommandAnalyzerPrint.class);
        classes.add(CommandAnalyzerReturn.class);
        classes.add(CommandAnalyzerSub.class);
        classes.add(CommandAnalyzerUse.class);
        classes.add(CommandAnalyzerWend.class);
        classes.add(CommandAnalyzerWhile.class);
        for (Class<?> klass : classes) {
            AbstractCommandAnalyzer o = (AbstractCommandAnalyzer) klass
                    .newInstance();
            String name = o.getName();
            Assert.assertEquals(klass.getSimpleName().substring(15).toUpperCase(),
                    name);
        }
    }
}
