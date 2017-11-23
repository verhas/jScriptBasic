package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private Factory factory;
    private LexicalElement lexicalElement;

    private static boolean lineToIgnore(String lexString) {
        return lexString.equals("\n") || lexString.equals("'")
                || lexString.equalsIgnoreCase("REM");
    }

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

    public LexicalElement getLexicalElement() {
        return this.lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    @Override
    public BuildableProgram analyze() throws AnalysisException {
        try {
            BuildableProgram buildableProgram = FactoryUtility.getProgram(getFactory());
            buildableProgram.reset();
            LexicalAnalyzer lexicalAnalyzer = FactoryUtility.getLexicalAnalyzer(getFactory());
            final CommandFactory commandFactory = FactoryUtility.getCommandFactory(getFactory());
            lexicalElement = lexicalAnalyzer.peek();
            while (lexicalElement != null) {
                if (lexicalElement.isSymbol()) {
                    lexicalAnalyzer.get();
                    final String lexString = lexicalElement.getLexeme();
                    if (lineToIgnore(lexString)) {
                        consumeIgnoredLine(lexicalAnalyzer, lexString);
                    } else {
                        buildableProgram.addCommand(commandFactory.create(lexString));
                    }
                } else {
                    buildableProgram.addCommand(commandFactory.create(null));
                }
                this.lexicalElement = lexicalAnalyzer.peek();
            }
            buildableProgram.postprocess();
            return buildableProgram;
        } catch (CommandFactoryException e) {
            throw new GenericSyntaxException(e.getMessage(), lexicalElement, e);
        }
    }

    private void consumeIgnoredLine(LexicalAnalyzer lexicalAnalyzer, String lexString) throws AnalysisException {
        while (!lexString.equals("\n")) {
            LexicalElement le = lexicalAnalyzer.get();
            if (le == null) {
                break;
            } else {
                lexString = le.getLexeme();
            }
        }
    }
}
