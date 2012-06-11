package com.scriptbasic.factories;

import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.interfaces.ExpressionAnalyzer;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.FactoryManaged;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.interfaces.SyntaxAnalyzer;
import com.scriptbasic.interfaces.TagAnalyzer;
import com.scriptbasic.lexer.ScriptBasicLexicalAnalyzer;
import com.scriptbasic.syntax.BasicProgram;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;
import com.scriptbasic.syntax.expression.BasicExpressionAnalyzer;
import com.scriptbasic.syntax.expression.BasicExpressionListAnalyzer;
import com.scriptbasic.syntax.expression.BasicTagAnalyzer;

/**
 * This extension of the concrete class {@see GenericFactory} does return an
 * object instance even if there was no call to {@see #create(Class, Class)}
 * previously.
 * <p>
 * To do that the class contains a list of classes for the interfaces that are
 * used by the BASIC interpreter and this way it knows which class (implementing
 * the interface by the way) should be instantiated by the {@see Factory} when
 * {@see #get(Class)} is called.
 * 
 * @author Peter Verhas
 * @date June 8, 2012
 */
public class BasicFactory extends GenericFactory {

    private static Map<Class<? extends FactoryManaged>, Class<? extends FactoryManaged>> classMapping = new HashMap<Class<? extends FactoryManaged>, Class<? extends FactoryManaged>>();
    static {
        classMapping.put(SyntaxAnalyzer.class, BasicSyntaxAnalyzer.class);
        classMapping.put(ExpressionAnalyzer.class,
                BasicExpressionAnalyzer.class);
        classMapping.put(ExpressionListAnalyzer.class,
                BasicExpressionListAnalyzer.class);
        classMapping.put(Program.class, BasicProgram.class);
        classMapping.put(TagAnalyzer.class, BasicTagAnalyzer.class);
        classMapping.put(LexicalAnalyzer.class, ScriptBasicLexicalAnalyzer.class);
    }

    /**
     * {@inheritDoc}
     * 
     * This version of the method creates a new instance if there is no object
     * in the factory associated with the interface passed as argument.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends FactoryManaged> T get(Class<T> interfac) {
        T object = super.get(interfac);
        if (object == null) {
            // TODO how could we avoid this unchecked mapping?
            Class<? extends T> klass = (Class<? extends T>) classMapping
                    .get(interfac);
            if (klass == null) {
                throw new BasicInterpreterInternalError(
                        "There is no class associated to the interface "
                                + interfac);
            }
            create(interfac, klass);
            object = super.get(interfac);
        }
        return object;
    }
}
