package com.scriptbasic.interfaces;

/**
 * Every object that represents a structure that is nested in the source code
 * should implement this interface. For example {@link com.scriptbasic.executors.commands.CommandWhile} implements
 * this and as such any new instance of it can be stored by a {@link NestedStructureHouseKeeper}
 * object during compilation.
 *
 * @author Peter Verhas
 * date June 8, 2012
 */
public interface NestedStructure {

}
