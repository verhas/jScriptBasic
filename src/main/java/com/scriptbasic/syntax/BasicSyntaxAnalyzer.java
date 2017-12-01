package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.interfaces.*;

public final class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private final CommandFactory commandFactory;
    private LexicalElement lexicalElement;

    public BasicSyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer, CommandFactory commandFactory) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.commandFactory = commandFactory;
    }

    private static boolean lineToIgnore(String lexString) {
        return lexString.equals("\n") || lexString.equals("'")
                || lexString.equalsIgnoreCase("REM");
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
            BuildableProgram buildableProgram = new BasicProgram();
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
            throw new BasicSyntaxException(e.getMessage(), lexicalElement, e);
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
