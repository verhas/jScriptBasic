package com.scriptbasic.executors.leftvalues;

import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.interfaces.RightValue;

/**
 * A left value that is a simple variable. This means that there is a simple
 * identifier at the place where the left value is expected and the value has to
 * be assigned to that variable represented by the identifier. The actual
 * variable can be local in a function or global in the BASIC program, but
 * nothing else.
 * 
 * @author Peter Verhas
 * @date June 13, 2012
 *
 */
public class BasicLeftValue extends AbstractLeftValue {

	/**
	 * The identifier that is the name of the local or global variable.
	 */
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	List<LeftValueModifier> modifiers = new LinkedList<LeftValueModifier>();
	
	public List<LeftValueModifier> getModifiers() {
		return modifiers;
	}

	public void addModifier(LeftValueModifier modifier){
		modifiers.add(modifier);
	}
	
	@Override
	public void setValue(final RightValue rightValue) throws Exception {
		// TODO Auto-generated method stub

	}

}
