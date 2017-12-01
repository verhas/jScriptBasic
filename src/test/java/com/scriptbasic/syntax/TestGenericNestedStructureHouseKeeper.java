package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestGenericNestedStructureHouseKeeper {
    private final LexicalAnalyzer lexicalAnalyzer = new NullLexicalAnalyzer();
    private NestedStructure dummy = new NestedStructure() {
    };

    private NestedStructureHouseKeeper newSut() {
        return new GenericNestedStructureHouseKeeper(lexicalAnalyzer);
    }

    @Test
    public void canPushAStructureOnTheStack() {
        NestedStructureHouseKeeper sut = newSut();
        sut.push(dummy);
    }

    @Test
    public void popsBackTheSameObjectThatWasPushed() throws AnalysisException {
        NestedStructureHouseKeeper sut = newSut();
        sut.push(dummy);
        NestedStructure result = sut.pop(dummy.getClass());
        assertEquals(dummy, result);
    }

    @Test
    public void popsBackTheSameObjectThatWasPushedByOtherClass() throws AnalysisException {
        NestedStructureHouseKeeper sut = newSut();
        NestedStructure other = new NestedStructure() {
        };
        sut.push(other.getClass(), dummy);
        NestedStructure result = sut.pop(other.getClass());
        assertEquals(dummy, result);
    }

    @Test(expected = BasicSyntaxException.class)
    public void throwsExceptionIfExpectingDifferentClass() throws AnalysisException {
        NestedStructureHouseKeeper sut = newSut();
        sut.push(dummy);
        sut.pop(new NestedStructure() {
        }.getClass());
    }

    @Test(expected = BasicSyntaxException.class)
    public void throwsExceptionIfExpectingDifferentOtherClass() throws AnalysisException {
        NestedStructureHouseKeeper sut = newSut();
        NestedStructure other = new NestedStructure() {
        };
        sut.push(other.getClass(), dummy);
        sut.pop(new NestedStructure() {
        }.getClass());
    }
}
