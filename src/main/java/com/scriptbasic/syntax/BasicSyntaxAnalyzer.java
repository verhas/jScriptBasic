package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.*;
import com.scriptbasic.spi.Command;

public final class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
  private final LexicalAnalyzer lexicalAnalyzer;
  private final CommandFactory commandFactory;
  private LexicalElement lexicalElement;

  public BasicSyntaxAnalyzer(final LexicalAnalyzer lexicalAnalyzer, final CommandFactory commandFactory) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.commandFactory = commandFactory;
  }

  private static boolean lineToIgnore(final String lexString) {
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
    final BuildableProgram buildableProgram = new BasicProgram();
    lexicalElement = lexicalAnalyzer.peek();
    while (lexicalElement != null) {
      if (lexicalElement.isSymbol()) {
        lexicalAnalyzer.get();
        final String lexString = lexicalElement.getLexeme();
        if (lineToIgnore(lexString)) {
          consumeIgnoredLine(lexicalAnalyzer, lexString);
        } else {
          final Command newCommand = commandFactory.create(lexString);
          if (newCommand != null) {
            buildableProgram.addCommand(newCommand);
          }
        }
      } else {
        final Command newCommand = commandFactory.create(null);
        if (newCommand != null) {
          buildableProgram.addCommand(newCommand);
        }
      }
      this.lexicalElement = lexicalAnalyzer.peek();
    }
    buildableProgram.postprocess();
    return buildableProgram;
  }

  private void consumeIgnoredLine(final LexicalAnalyzer lexicalAnalyzer, String lexString) throws AnalysisException {
    while (!lexString.equals("\n")) {
      final LexicalElement le = lexicalAnalyzer.get();
      if (le == null) {
        break;
      } else {
        lexString = le.getLexeme();
      }
    }
  }
}
