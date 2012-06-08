package com.scriptbasic.syntax;

import static com.scriptbasic.utility.FactoryUtilities.getLexicalAnalyzer;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.commandanalyzers.BasicCommandFactory;

public class BasicSyntaxAnalyzer implements SyntaxAnalyzer {

    private BasicSyntaxAnalyzer(){
    }

    private BasicProgram program;
    private LexicalElement lexicalElement;

    public LexicalElement getLexicalElement() {
        return this.lexicalElement;
    }

    public void setLexicalElement(final LexicalElement lexicalElement) {
        this.lexicalElement = lexicalElement;
    }

    @Override
    public Program analyze() throws SyntaxException, LexicalException {
        LexicalAnalyzer lexicalAnalyzer = getLexicalAnalyzer();
        this.lexicalElement = lexicalAnalyzer.get();
        final BasicCommandFactory basicCommandFactory = new BasicCommandFactory();
        basicCommandFactory.setSyntaxAnalyzer(this);
        while (this.lexicalElement != null) {
            if (this.lexicalElement.isSymbol()) {
                this.program.addCommand(basicCommandFactory
                        .create(this.lexicalElement.get()));
            } else {
                this.program.addCommand(basicCommandFactory.create(null));
            }
            this.lexicalElement = lexicalAnalyzer.get();
        }
        return this.program;
    }
}
