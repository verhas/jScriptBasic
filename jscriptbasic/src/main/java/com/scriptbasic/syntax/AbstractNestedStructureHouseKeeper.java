package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.NestedStructure;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;

public abstract class AbstractNestedStructureHouseKeeper implements
        NestedStructureHouseKeeper {

    @Override
    public void push(NestedStructure element) {
        push(element.getClass(), element);

    }

}
