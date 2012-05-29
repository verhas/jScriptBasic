package com.scriptbasic.syntax.commandanalyzers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;
import com.scriptbasic.syntax.GenericSyntaxException;
import com.scriptbasic.syntax.KeywordNotImplemented;

public class BasicCommandFactory implements CommandFactory {

    BasicSyntaxAnalyzer syntaxAnalyzer;

    public void setSyntaxAnalyzer(BasicSyntaxAnalyzer basicSyntaxAnalyzer) {
        syntaxAnalyzer = basicSyntaxAnalyzer;

    }

    private static Map<String, CommandAnalyzer> classMap = new HashMap<String, CommandAnalyzer>();
    static {
        classMap.put("while", new CommandAnalyzerWhile());
    }
    private static List<CommandAnalyzer> classList = new LinkedList<CommandAnalyzer>();
    static {
        classList.add(new CommandAnalyzerLet());
        classList.add(new CommandAnalyzerCall());
    }

    @Override
    public Command create(String commandKeyword) throws SyntaxException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private Command create() throws SyntaxException {
        for (CommandAnalyzer commandAnalyzer : classList) {
            commandAnalyzer.analyze();
            Command command = commandAnalyzer.getCommand();
            if (command != null) {
                return command;
            }
        }
        SyntaxException se = new GenericSyntaxException(
                "Generic syntax exception");
        se.setLocation(syntaxAnalyzer.getLexicalElement());
        throw se;
    }

    private Command createFromStartingSymbol(String commandKeyword)
            throws SyntaxException {
        if (!classMap.containsKey(commandKeyword)) {
            SyntaxException se = new KeywordNotImplemented(commandKeyword);
            se.setLocation(syntaxAnalyzer.getLexicalElement());
            throw se;
        }

        CommandAnalyzer commandAnalyzer = classMap.get(commandKeyword);
        commandAnalyzer.setSyntaxAnalyzer(syntaxAnalyzer);
        commandAnalyzer.analyze();
        return commandAnalyzer.getCommand();
    }
}
