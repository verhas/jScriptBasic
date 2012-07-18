package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BuildableProgram;
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

    private BasicSyntaxAnalyzer() {
    }

    private LexicalElement lexicalElement;

    public LexicalElement getLexicalElement() {
        return this.lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    private static boolean lineToIgnore(String lexString) {
        return lexString.equals("\n") || lexString.equals("'")
                || lexString.equalsIgnoreCase("REM");
    }

    @Override
    public BuildableProgram analyze() throws AnalysisException {
        try {
            BuildableProgram buildableProgram = FactoryUtility
                    .getProgram(getFactory());
            LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                    .getLexicalAnalyzer(getFactory());
            final CommandFactory commandFactory = FactoryUtility
                    .getCommandFactory(getFactory());
            this.lexicalElement = lexicalAnalyzer.peek();
            while (this.lexicalElement != null) {
                if (this.lexicalElement.isSymbol()) {
                    lexicalAnalyzer.get();
                    String lexString = this.lexicalElement.getLexeme();
                    if (lineToIgnore(lexString)) {
                        while (!lexString.equals("\n")) {
                            LexicalElement le = lexicalAnalyzer.get();
                            if (le == null) {
                                break;
                            } else {
                                lexString = le.getLexeme();
                            }
                        }
                    } else {
                        buildableProgram.addCommand(commandFactory
                                .create(this.lexicalElement.getLexeme()));
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
}
