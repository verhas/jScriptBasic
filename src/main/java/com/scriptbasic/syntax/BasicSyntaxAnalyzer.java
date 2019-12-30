package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.*;

public final class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private final CommandFactory commandFactory;
    private final NestedStructureHouseKeeper nestedStructureHouseKeeper;
    private LexicalElement lexicalElement;    

    public BasicSyntaxAnalyzer(final LexicalAnalyzer lexicalAnalyzer, final CommandFactory commandFactory, 
                               final NestedStructureHouseKeeper nestedStructureHouseKeeper) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.commandFactory = commandFactory;
        this.nestedStructureHouseKeeper = nestedStructureHouseKeeper;
    }

    public static boolean lineToIgnore(final String lexString) {
        return lexString.equals("\n") || lexString.equals("'")
                || lexString.equalsIgnoreCase(ScriptBasicKeyWords.KEYWORD_REM);
    }

    public LexicalElement getLexicalElement() {
        return this.lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    @Override
    public BuildableProgram analyze() throws AnalysisException {
        final var buildableProgram = new BasicProgram();
        lexicalElement = lexicalAnalyzer.peek();
        while (lexicalElement != null) {
            if (lexicalElement.isSymbol()) {
                lexicalAnalyzer.get();
                final var lexString = lexicalElement.getLexeme();
                if (lineToIgnore(lexString)) {
                    consumeIgnoredLine(lexicalAnalyzer, lexString);
                } else {
                    final var newCommand = commandFactory.create(lexString);
                    if (newCommand != null) {
                        buildableProgram.addCommand(newCommand);
                    }
                }
            } else {
                final var newCommand = commandFactory.create(null);
                if (newCommand != null) {
                    buildableProgram.addCommand(newCommand);
                }
            }
            this.lexicalElement = lexicalAnalyzer.peek();
        }
        nestedStructureHouseKeeper.checkFinalState();
        buildableProgram.postprocess();
        return buildableProgram;
    }

    public static void consumeIgnoredLine(final LexicalAnalyzer lexicalAnalyzer, String lexString) throws AnalysisException {
        while (!lexString.equals("\n")) {
            final var le = lexicalAnalyzer.get();
            if (le == null) {
                break;
            } else {
                lexString = le.getLexeme();
            }
        }
    }
}
