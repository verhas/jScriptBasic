package com.scriptbasic.testprograms;

import com.scriptbasic.Executor;
import org.junit.Test;

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
