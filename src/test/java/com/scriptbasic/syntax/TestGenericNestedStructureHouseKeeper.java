package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGenericNestedStructureHouseKeeper {
    private final LexicalAnalyzer lexicalAnalyzer = new NullLexicalAnalyzer();
    private final NestedStructure dummy = new NestedStructure() {
    };

    private NestedStructureHouseKeeper newSut() {
        return new GenericNestedStructureHouseKeeper(lexicalAnalyzer);
    }

    @Test
    public void canPushAStructureOnTheStack() {
        final var sut = newSut();
        sut.push(dummy);
    }

    @Test
    public void popsBackTheSameObjectThatWasPushed() throws AnalysisException {
        final var sut = newSut();
        sut.push(dummy);
        final var result = sut.pop(dummy.getClass());
        assertEquals(dummy, result);
    }

    @Test
    public void popsBackTheSameObjectThatWasPushedByOtherClass() throws AnalysisException {
        final var sut = newSut();
        final var other = new NestedStructure() {
        };
        sut.push(other.getClass(), dummy);
        final NestedStructure result = sut.pop(other.getClass());
        assertEquals(dummy, result);
    }

    @Test()
    public void throwsExceptionIfExpectingDifferentClass() {
        final var sut = newSut();
        sut.push(dummy);
        Assertions.assertThrows(BasicSyntaxException.class, () ->
                sut.pop(new NestedStructure() {
                }.getClass()));
    }

    @Test()
    public void throwsExceptionIfExpectingDifferentOtherClass() {
        final var sut = newSut();
        final var other = new NestedStructure() {
        };
        sut.push(other.getClass(), dummy);
        Assertions.assertThrows(BasicSyntaxException.class, () ->
                sut.pop(new NestedStructure() {
                }.getClass()));
    }
}
