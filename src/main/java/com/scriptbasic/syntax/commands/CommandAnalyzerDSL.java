package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.executors.commands.CommandCall;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.spi.Command;

import java.util.LinkedList;
import java.util.List;

public class CommandAnalyzerDSL extends AbstractCommandAnalyzer {
  private List<DslLine> dslLines = new LinkedList<>();

  protected CommandAnalyzerDSL(Context ctx) {
    super(ctx);
  }

  // sentence "assert that $expression is the same as $expression" call assert

  @Override
  public Command analyze() throws AnalysisException {
    ctx.lexicalAnalyzer.resetLine();
    if (ctx.lexicalAnalyzer.get().isSymbol("sentence")) {
      defineDSLRule();
      return null;
    } else {
      for (final DslLine line : dslLines) {
        ctx.lexicalAnalyzer.resetLine();
        Command command = analyzeWith(line);
        if (command != null) {
          return command;
        }
      }
      throw new BasicSyntaxException("Can not analyze the line as DSL.");
    }
  }

  private Command analyzeWith(DslLine line) {
    final GenericExpressionList expressionList = new GenericExpressionList();
    for (String match : line.syntaxElements) {

      if (match.equalsIgnoreCase("$expression")) {
        final Expression expression;
        try {
          expression = analyzeExpression();
        } catch (AnalysisException ignored) {
          return null;
        }
        expressionList.add(expression);
      } else {
        final LexicalElement lexicalElement;
        try {
          lexicalElement = ctx.lexicalAnalyzer.get();
        } catch (AnalysisException ignored) {
          return null;
        }
        if (!lexicalElement.getLexeme().equalsIgnoreCase(match)) {
          return null;
        }
      }
    }
    final FunctionCall functionCall = new FunctionCall();
    functionCall.setVariableName(line.methodName);
    functionCall.setExpressionList(expressionList);
    return new CommandCall(functionCall);
  }

  private void defineDSLRule() throws AnalysisException {
    final LexicalElement actualSentence = ctx.lexicalAnalyzer.get();
    if (!actualSentence.isString()) {
      throw new BasicSyntaxException("There should be a string after the keyword 'sentence'");
    }
    final String sentence = actualSentence.stringValue();
    final LexicalElement callsKW = ctx.lexicalAnalyzer.get();
    if (!callsKW.isSymbol("call")) {
      throw new BasicSyntaxException("missing keyword 'call' after string in command 'sentence'");
    }
    final LexicalElement functionNameLexicalElement = ctx.lexicalAnalyzer.get();
    if (!functionNameLexicalElement.isIdentifier()) {
      throw new BasicSyntaxException("there should be a function name after the keyword 'call' defining a sentenceó");
    }
    consumeEndOfLine();
    dslLines.add(new DslLine(functionNameLexicalElement.getLexeme(), sentence.split("\\s+")));
  }

  private class DslLine {
    final String methodName;
    final String[] syntaxElements;

    private DslLine(String methodName, String[] syntaxElements) {
      this.methodName = methodName;
      this.syntaxElements = syntaxElements;
    }
  }

}
