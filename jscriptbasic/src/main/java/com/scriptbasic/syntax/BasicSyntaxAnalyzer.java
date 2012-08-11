package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

    private LexicalElement lexicalElement;

    public LexicalElement getLexicalElement() {
        return this.lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    private static boolean lineToIgnore(final String lexString) {
        return lexString.equals("\n") || lexString.equals("'")
                || lexString.equalsIgnoreCase("REM");
    }

    private static void skipLine(LexicalAnalyzer lexicalAnalyzer,
            String lexString) throws AnalysisException {
        while (!lexString.equals("\n")) {
            final LexicalElement le = lexicalAnalyzer.get();
            if (le == null) {
                return;
            } else {
                lexString = le.getLexeme();
            }
        }
    }

    private static boolean eof(LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        return lexicalAnalyzer.peek() == null;
    }

    @Override
    public BuildableProgram analyze() throws AnalysisException {
        try {
            final BuildableProgram buildableProgram = FactoryUtility
                    .getProgram(getFactory());
            buildableProgram.reset();
            final LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                    .getLexicalAnalyzer(getFactory());
            final CommandFactory commandFactory = FactoryUtility
                    .getCommandFactory(getFactory());
            this.lexicalElement = lexicalAnalyzer.peek();
            while (this.lexicalElement != null) {
                Command command = null;
                if (this.lexicalElement.isSymbol()) {
                    lexicalAnalyzer.get();
                    String lexString = this.lexicalElement.getLexeme();
                    if (lineToIgnore(lexString)) {
                        skipLine(lexicalAnalyzer, lexString);
                        if (eof(lexicalAnalyzer)) {
                            break;
                        }
                    } else {
                        command = commandFactory.create(this.lexicalElement
                                .getLexeme());
                    }
                } else {
                    command = commandFactory.create(null);
                }
                if (command != null) {
                    buildableProgram.addCommand(command);
                    command.setFileName(lexicalElement.getFileName());
                    command.setLineNumber(lexicalElement.getLineNumber());
                    command.setPosition(lexicalElement.getPosition());
                }
                this.lexicalElement = lexicalAnalyzer.peek();
            }
            buildableProgram.postprocess();
            return buildableProgram;
        } catch (final CommandFactoryException e) {
            throw new GenericSyntaxException(e.getMessage(), lexicalElement, e);
        }
    }
}
