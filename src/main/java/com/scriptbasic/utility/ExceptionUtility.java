package com.scriptbasic.utility;

public final class ExceptionUtility {

	private ExceptionUtility() {
		UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
	}

	/**
	 * This method checks that the exception is of a certain type. If the exception is one of the types
	 * listed as arguments then the method returns. If the exception is none of the types listed then
	 * the method throws the exception passed as first argument.
	 * <p>
	 * This utility method is used to mimic the Java 7
	 * <pre>
	 *  catch( E1 | E2 | E3 e){
	 *   ...
	 *   }
	 * </pre> 
	 * 
	 * construct. The pattern instead (compatible with Java 6) is
	 * 
	 * <pre>
	 *  catch(Exception e){
	 *   ExceptionUtility.assertException(e, E1.class, E2.class, E3.class);
	 *   ...
	 *   }
	 * </pre>
	 * 
	 * 
	 * @param e
	 * @param classes
	 * @throws Exception
	 */
	public static void assertExceptionType(Exception e,
			Class<? extends Exception>... classes) throws Exception {
		for (Class<? extends Exception> klass : classes) {
			if (klass.isInstance(e)) {
				return;
			}
		}
		throw e;
	}

}
