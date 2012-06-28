/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.exceptions.GenericSyntaxException;

/**
 * @author Peter Verhas
 * @date Jun 28, 2012
 * 
 */
public class Klass {
    private Klass() {
        UtilityUtility.assertUtilityClass();
    }

    public static Class<?> forName(String s) throws GenericSyntaxException {
        Class<?> klass = null;
        switch (s) {
        case "byte":
            klass = byte.class;
        case "short":
            klass = short.class;
            break;
        case "char":
            klass = char.class;
            break;
        case "double":
            klass = double.class;
            break;
        case "float":
            klass = float.class;
            break;
        case "long":
            klass = long.class;
            break;
        case "int":
            klass = int.class;
            break;
        case "boolean":
            klass = boolean.class;
            break;
        default:
            try {
                klass = Class.forName(s);
            } catch (ClassNotFoundException e) {
                throw new GenericSyntaxException("Can not get class " + s, e);
            }
        }
        return klass;
    }
}
