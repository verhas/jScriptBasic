package com.scriptbasic.testprograms;

import org.junit.Test;

import com.scriptbasic.Executor;

public class TestFunctions {
	private static void codeTest(String fileName)
			throws Exception {
		Executor e = new Executor();
		e.setMap(null);
		e.execute(fileName);
	}
	
	
	@Test
	public void testMath() throws Exception{
		codeTest("TestMath.bas");
	}
}
