package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.commandanalyzers.BasicCommandFactory;

public class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;

    public LexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public void setLexicalAnalyzer(final LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void setProgram(final BasicProgram program) {
        this.program = program;
    }

    private BasicProgram program;
    private LexicalElement lexicalElement;

    public LexicalElement getLexicalElement() {
        return lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    @Override
    public void analyze() throws SyntaxException, LexicalException {
        lexicalElement = lexicalAnalyzer.get();
        BasicCommandFactory basicCommandFactory = new BasicCommandFactory();
        basicCommandFactory.setSyntaxAnalyzer(this);
        while (lexicalElement != null) {
            if (lexicalElement.isSymbol()) {
                program.addCommand(basicCommandFactory.create(lexicalElement
                        .get()));
            } else {
                program.addCommand(basicCommandFactory.create(null));
            }
            lexicalElement = lexicalAnalyzer.get();
        }
    }

    @Override
    public Program getProgram() {
        return program;
    }

}
