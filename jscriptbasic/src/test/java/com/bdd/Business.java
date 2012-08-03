/**
 * 
 */
package com.bdd;

/**
 * @author Peter Verhas
 * @date Aug 3, 2012
 *
 */
public abstract class Business {

    abstract public void given();
    abstract public void when();
    abstract public void then();
    public void execute(){
        given();
        when();
        then();
    }
    
}
