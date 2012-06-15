package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.LeftValueListAnalyzer;
import com.scriptbasic.syntax.AbstractGenericListAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class BasicLeftValueListAnalyzer
		extends
		AbstractGenericListAnalyzer<LeftValueList, GenericLeftValueList, LeftValue, LeftValueAnalyzer>
		implements LeftValueListAnalyzer {
	private Factory factory;

	private BasicLeftValueListAnalyzer() {
	}

	@Override
	public Factory getFactory() {
		return factory;
	}

	@Override
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Override
	public LeftValueList analyze() throws AnalysisException {
		setList(new GenericLeftValueList());
		setAnalyzer(FactoryUtilities.getLeftValueAnalyzer(getFactory()));
		return super.analyze();
	}

}
