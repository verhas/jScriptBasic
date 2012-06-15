package com.scriptbasic.syntax;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class BasicSyntaxAnalyzer implements SyntaxAnalyzer {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
	public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private BasicSyntaxAnalyzer() {
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
    public Program analyze() throws AnalysisException {
        try {
            LexicalAnalyzer lexicalAnalyzer = FactoryUtilities
                    .getLexicalAnalyzer(getFactory());
            this.lexicalElement = lexicalAnalyzer.get();
            final CommandFactory commandFactory = FactoryUtilities.getCommandFactory(getFactory());
            while (this.lexicalElement != null) {
                if (this.lexicalElement.isSymbol()) {
                    this.program.addCommand(commandFactory
                            .create(this.lexicalElement.get()));
                } else {
                    this.program.addCommand(commandFactory.create(null));
                }
                this.lexicalElement = lexicalAnalyzer.get();
            }
            return this.program;
        } catch (CommandFactoryException e) {
            throw new GenericSyntaxException(e.getMessage(), lexicalElement);
        }
    }
}
