package com.scriptbasic.interfaces;

import com.scriptbasic.context.CompilerContext;

public interface Compiler {
    default String toJava(CompilerContext cc){
        return "";
    }

    default CodeCreator fragment(){
        return new CodeCreator();
    }

    public class CodeCreator {
        final private StringBuilder sb = new StringBuilder();
        public CodeCreator add(final String fragment, final Object... parameters) {
            sb.append(fragment.formatted((Object[]) parameters));
            return this;
        }

        public String close() {
            return sb.toString();
        }
    }
}
