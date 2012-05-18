package com.scriptbasic.lexer;

import java.io.StringReader;

import junit.framework.TestCase;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.readers.GenericReader;

public class TestBasicLexicalAnalyzer extends TestCase {
	public TestBasicLexicalAnalyzer(String testName) {
		super(testName);
	}

	private class TestLE extends BasicLexicalElement {
		private TestLE(String lexeme, Integer type, Double doubleValue,
				Long longValue, String stringValue) {
			setType(type);
			setLexeme(lexeme);
			switch (type) {
			case LexicalElement.TYPE_DOUBLE:
				setDoubleValue(doubleValue);
				break;
			case LexicalElement.TYPE_LONG:
				setLongValue(longValue);
				break;
			case LexicalElement.TYPE_STRING:
				setStringValue(stringValue);
				break;
			case LexicalElement.TYPE_SYMBOL:
			case LexicalElement.TYPE_IDENTIFIER:
				break;
			}
		}

	}

	private TestLE ID(final String name) {
		return new TestLE(name, LexicalElement.TYPE_IDENTIFIER, null, null,
				null);
	}

	private TestLE LONG(final String s) {
		Long l = Long.parseLong(s);
		return new TestLE(s, LexicalElement.TYPE_LONG, null, l, null);
	}

	private TestLE DOUBLE(final String s) {
		Double d = Double.parseDouble(s);
		return new TestLE(s, LexicalElement.TYPE_DOUBLE, d, null, null);
	}

	private static final StringBuilder apo = new StringBuilder("\"");
	private static final StringBuilder apoE = new StringBuilder("\\\"");
	private static final StringBuilder newLine = new StringBuilder("\n");
	private static final StringBuilder newLineE = new StringBuilder("\\n");
	private static final StringBuilder tab = new StringBuilder("\t");
	private static final StringBuilder tabE = new StringBuilder("\\t");
	private static final StringBuilder cr = new StringBuilder("\r");
	private static final StringBuilder crE = new StringBuilder("\\r");
	private static final StringBuilder bs = new StringBuilder("\\");
	private static final StringBuilder bsE = new StringBuilder("\\\\");

	private String string2Lexeme(String s) {
		s = s.replace(bs, bsE);
		s = s.replace(newLine, newLineE);
		s = s.replace(tab, tabE);
		s = s.replace(cr, crE);
		return s;
	}

	private String sstring2Lexeme(String s) {
		s = string2Lexeme(s);
		s = s.replace(apo, apoE);
		return s;
	}

	private TestLE SSTRING(String s) {
		String lexeme = sstring2Lexeme(s);
		return SSTRING(s, lexeme);
	}

	private TestLE SSTRING(String s, String lexeme) {
		return new TestLE("\"" + lexeme + "\"", LexicalElement.TYPE_STRING,
				null, null, s);
	}

	private TestLE MSTRING(String s) {
		String lexeme = string2Lexeme(s);
		return MSTRING(s, lexeme);
	}

	private TestLE MSTRING(String s, String lexeme) {
		return new TestLE("\"\"\"" + lexeme + "\"\"\"",
				LexicalElement.TYPE_STRING, null, null, s);
	}

	private TestLE VSTRING(String s, String lexeme, boolean multiline) {
		return multiline ? MSTRING(s, lexeme) : SSTRING(s, lexeme);
	}

	private TestLE VSTRING(String s, boolean multiline) {
		return multiline ? MSTRING(s) : SSTRING(s);
	}

	private TestLE SYMBOL(String s) {
		return new TestLE(s, LexicalElement.TYPE_SYMBOL, null, null, null);
	}

	private void assertLexicals(LexicalElement[] lea, LexicalAnalyzer la)
			throws LexicalException {
		for (LexicalElement le : lea) {
			LexicalElement le1 = la.get();
			assertNotNull("there are not enough lexical elements, expecting "
					+ le.get(), le1);
			assertEquals("different types of lexemes " + le.get() + " vs "
					+ le1.get(), le.type(), le1.type());
			assertEquals("different lexemes " + le.get() + " vs " + le1.get(),
					le.get(), le1.get());
			switch (le.type()) {
			case LexicalElement.TYPE_DOUBLE:
				assertEquals("different double values", le.doubleValue(),
						le1.doubleValue());
				break;
			case LexicalElement.TYPE_LONG:
				assertEquals("different long values", le.longValue(),
						le1.longValue());
				break;
			case LexicalElement.TYPE_STRING:
				assertEquals("different string values", le.stringValue(),
						le1.stringValue());
				break;
			case LexicalElement.TYPE_SYMBOL:
			case LexicalElement.TYPE_IDENTIFIER:
				break;
			}
		}
	}

	private LexicalAnalyzer createStringReading(String s) {
		java.io.Reader r = new StringReader(s);
		GenericReader reader = new GenericReader();
		reader.set(r);
		reader.setSourceProvider(null);
		reader.set((String)null);
		LexicalAnalyzer la = new BasicLexicalAnalyzer();
		la.set(reader);
		return la;
	}

	private LexicalAnalyzer createMStringReading(String s) {
		return createStringReading("\"\"\"" + s + "\"\"\"");
	}

	private LexicalAnalyzer createSStringReading(String s) {
		return createStringReading("\"" + s + "\"");
	}

	private LexicalAnalyzer createVStringReading(String s, boolean multiline) {
		if (multiline) {
			return createMStringReading(s);
		} else {
			return createSStringReading(s);
		}
	}

	public void testString() throws LexicalException {
		boolean multiline = false;
		do {
			// empty string
			assertLexicals(new LexicalElement[] { VSTRING("", multiline) },
					createVStringReading("", multiline));
			// string with a " in it
			assertLexicals(
					new LexicalElement[] { VSTRING("\"", "\\\"", multiline) },
					createVStringReading("\\\"", multiline));
			// string with a " and a space without escaping the "
			// works only in multi-line string
			if (multiline) {
				assertLexicals(
						new LexicalElement[] { VSTRING("\" ", "\" ", multiline) },
						createVStringReading("\" ", multiline));
			}
			// string with a " and a space escaping the "
			assertLexicals(
					new LexicalElement[] { VSTRING("\" ", "\\\" ", multiline) },
					createVStringReading("\\\" ", multiline));
			// string with a new line in it
			assertLexicals(
					new LexicalElement[] { VSTRING("\n", "\\n", multiline) },
					createVStringReading("\\n", multiline));
			// one character and a new line in it
			assertLexicals(
					new LexicalElement[] { VSTRING("a\n", "a\\n", multiline) },
					createVStringReading("a\\n", multiline));
			// string with a lf in it
			assertLexicals(
					new LexicalElement[] { VSTRING("\r", "\\r", multiline) },
					createVStringReading("\\r", multiline));
			// string with a tab in it
			assertLexicals(
					new LexicalElement[] { VSTRING("\t", "\\t", multiline) },
					createVStringReading("\\t", multiline));
			// string with a backslash in it
			assertLexicals(new LexicalElement[] { VSTRING("\\", multiline) },
					createVStringReading("\\\\", multiline));
			// string with a character not needing escape
			assertLexicals(
					new LexicalElement[] { VSTRING("R", "\\R", multiline) },
					createVStringReading("\\R", multiline));
			// string with a single normal character in it
			assertLexicals(new LexicalElement[] { VSTRING("x", multiline) },
					createVStringReading("x", multiline));

			// try multi-line and then exit
			multiline = !multiline;
		} while (multiline);

		assertLexicals(
				new LexicalElement[] { MSTRING("1\"\"\"\n2", "1\\\"\"\"\\n2") },
				createStringReading("\"\"\"1\\\"\"\"\\n2\"\"\""));
		assertLexicals(new LexicalElement[] { MSTRING("1\n2") },
				createStringReading("\"\"\"1\\n2\"\"\""));
		assertLexicals(new LexicalElement[] { MSTRING("\\") },
				createStringReading("\"\"\"\\\\\"\"\""));
		assertLexicals(new LexicalElement[] { MSTRING("1\"\n2") },
				createStringReading("\"\"\"1\"\\n2\"\"\""));
		assertLexicals(new LexicalElement[] { MSTRING("1\"\"\n2") },
				createStringReading("\"\"\"1\"\"\\n2\"\"\""));
	}

	public void testNewLine() throws LexicalException {
		assertLexicals(new LexicalElement[] { SYMBOL("\n") },
				createStringReading("\n"));
	}

	public void testFloatNumber() throws LexicalException {
		assertLexicals(new LexicalElement[] { DOUBLE("13e3") },
				createStringReading("13e3"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8") },
				createStringReading("13.8"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8e2") },
				createStringReading("13.8e2"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8e+2") },
				createStringReading("13.8e+2"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8e-2") },
				createStringReading("13.8e-2"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8E2") },
				createStringReading("13.8E2"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8E+2") },
				createStringReading("13.8E+2"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.8E-2") },
				createStringReading("13.8E-2"));
	}

	public void testFloatAndSomething() throws LexicalException {
		assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("e"),
				SYMBOL("+") }, createStringReading("13.2e+"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("e") },
				createStringReading("13.2e"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("E"),
				SYMBOL("+") }, createStringReading("13.2E+"));
		assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("E") },
				createStringReading("13.2E"));
		assertLexicals(new LexicalElement[] { LONG("13"), SYMBOL(".") },
				createStringReading("13."));
		assertLexicals(new LexicalElement[] { LONG("13"), SYMBOL("\n") },
				createStringReading("13\n"));
	}

	public void testUnterminatedString() throws LexicalException {
		try {
			assertLexicals(
					new LexicalElement[] { SSTRING("justAnything, should not check it, if it fails the test fails") },
					createStringReading("\""));
		} catch (UnterminatedStringException use) {
			// this is what we expect
		}
	}

	public void testSpaceSeparated() throws LexicalException {
		assertLexicals(
				new LexicalElement[] { ID("alma"), LONG("123"), ID("kšrte"),
						SYMBOL("<<"), SYMBOL(">="), SYMBOL("<<"),
						DOUBLE("12.3"), DOUBLE("13e3"), DOUBLE("12.3e2"),
						SSTRING("habakukk"), SYMBOL("<") },
				createStringReading("alma 123 kšrte << >= << 12.3 13e3 12.3e2 \"habakukk\" <"));
	}
}
