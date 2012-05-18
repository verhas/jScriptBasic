package com.scriptbasic.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.utility.CharacterCheck;

public class BasicLexicalAnalyzer implements LexicalAnalyzer {
	private Reader reader;

	public void set(Reader reader) {
		this.reader = reader;
	}

	private static String[] operators;
	private static int maxOperatorLength;

	static {
		try {
			loadOperators();
		} catch (IOException e) {
			operators = null;
		}
	}

	/**
	 * Load the operator lexemes from the file operators.txt
	 * <p>
	 * This file has to contain all the multi-character lexemes in an order that
	 * if lexeme A is prefix of lexeme B then B should preceede A. The simplest
	 * way to achieve this is to put longer lexemes first.
	 * <p>
	 * The lines that start with a letter are comments. Usually the # character
	 * is used as comment character, but # can be a valid start of a lexeme.
	 * <p>
	 * Spaces from the file are ignored and removed even if they appear between
	 * lexemes.
	 * <p>
	 * Empty lines are valid and are ignored.
	 * 
	 * @throws IOException
	 */
	private static void loadOperators() throws IOException {
		maxOperatorLength = 0;
		InputStream is = BasicLexicalAnalyzer.class
				.getResourceAsStream("operators.txt");
		if (is != null) {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = reader.readLine();
			ArrayList<String> ops = new ArrayList<String>();
			while (line != null) {
				line = line.replaceAll("\\s*", "");
				if (line.length() > 0 && !Pattern.matches("^\\w.*", line)) {
					ops.add(line);
					if (line.length() > maxOperatorLength) {
						maxOperatorLength = line.length();
					}
				}
				line = reader.readLine();
			}
			operators = ops.toArray(new String[ops.size()]);
		}
	}

	Deque<LexicalElement> lexicalElementQueue = new LinkedList<LexicalElement>();

	private void emptyLexicalElementQueue() {
		lexicalElementQueue = new LinkedList<LexicalElement>();
	}

	/**
	 * {@inheritDoc}
	 */
	public LexicalElement get() throws BasicLexicalException {
		if (lexicalElementQueue.isEmpty()) {
			readTheNextLine();
		}
		LexicalElement le = null;
		if (!lexicalElementQueue.isEmpty()) {
			le = lexicalElementQueue.removeFirst();
		}
		return le;
	}

	private boolean isTripleQuote() {
		boolean itIs = false;
		Integer second = reader.get();
		Integer third = reader.get();
		if (((Integer) (int) '"').equals(second)
				&& ((Integer) (int) '"').equals(third)) {
			itIs = true;
		} else {
			reader.pushBack(third);
			reader.pushBack(second);
		}
		return itIs;
	}

	private Integer skipWhiteSpaces(Integer ch) {
		while (ch != null && Character.isWhitespace(ch)
				&& !CharacterCheck.isNewLine(ch)) {
			ch = reader.get();
		}
		return ch;
	}

	private void readTheNextLine() throws BasicLexicalException {
		Boolean lineEndFound = false;
		for (Integer ch = reader.get(); ch != null && !lineEndFound; ch = reader
				.get()) {
			BasicLexicalElement le = null;
			ch = skipWhiteSpaces(ch);
			if (ch != null) {
				if (CharacterCheck.isNewLine(ch)) {
					reader.pushBack(ch);
					le = readOneCharacterLexeme();
					lineEndFound = true;
				} else if (Character.isUnicodeIdentifierStart(ch)) {
					reader.pushBack(ch);
					le = readIdentifier();
				} else if (Character.isDigit(ch)) {
					reader.pushBack(ch);
					le = readDecimalNumber();
				} else if (ch.equals((Integer) (int) '"')) {
					if (isTripleQuote()) {
						le = readMultiLineString();
					} else {
						le = readSingleLineString();
					}
				} else {
					le = readMultiCharacterLexeme(ch);
				}

				if (le != null) {
					lexicalElementQueue.add(le);
				}
			}
		}
		if (reader instanceof GenericHierarchicalReader) {
			GenericHierarchicalReader hreader = (GenericHierarchicalReader) reader;
			if (!lexicalElementQueue.isEmpty()) {
				LexicalElement le = lexicalElementQueue.getFirst();
				if (le.type() == LexicalElement.TYPE_SYMBOL
						&& le.get().equalsIgnoreCase("INCLUDE")) {
					lexicalElementQueue.removeFirst();
					le = lexicalElementQueue.removeFirst();
					// TODO check that there are no extra chars on the line
					if (le.type() == LexicalElement.TYPE_STRING) {
						SourceProvider sp = hreader.getSourceProvider();
						Reader childReader = sp.get(le.stringValue(),
								hreader.fileName());
						hreader.include(childReader);
						emptyLexicalElementQueue();
						readTheNextLine();
					} else {
						// TODO throw error that it is not string after the
						// INCLUDE
					}
				}
			}
		}
	}

	private void readAhead(StringBuilder sb, int charsToReadAhead) {
		Integer ch = reader.get();
		sb.appendCodePoint(ch);
		charsToReadAhead--;
		while (ch != null && charsToReadAhead > 0) {
			ch = reader.get();
			if (ch != null && !Character.isWhitespace(ch)) {
				sb.appendCodePoint(ch);
				charsToReadAhead--;
			}
		}
	}

	private void pushBack(StringBuilder sb, int charsToPushBack) {
		int pos = sb.length() - 1;
		while (charsToPushBack > 0) {
			reader.pushBack((Integer) (int) sb.charAt(pos));
			pos--;
			charsToPushBack--;
		}
	}

	private String convert(Integer ch) {
		StringBuilder sb = new StringBuilder(1);
		sb.appendCodePoint(ch);
		return sb.toString();
	}

	private BasicLexicalElement readMultiCharacterLexeme(Integer ch) {
		BasicLexicalElement le = BasicLexialElementFactory.create(reader,
				LexicalElement.TYPE_SYMBOL);
		StringBuilder sb = new StringBuilder(maxOperatorLength);
		reader.pushBack(ch);
		readAhead(sb, maxOperatorLength);
		String s = sb.toString();
		boolean found = false;
		for (String operator : operators) {
			if (s.startsWith(operator)) {
				pushBack(sb, s.length() - operator.length());
				le.setLexeme(operator);
				le.setStringValue(null);
				le.setType(LexicalElement.TYPE_SYMBOL);
				found = true;
				break;
			}
		}
		if (!found) {
			pushBack(sb, sb.length() - 1);
			le.setLexeme(convert(ch));
			le.setStringValue(null);
			le.setType(LexicalElement.TYPE_SYMBOL);
		}
		return le;
	}

	private static final int IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY = 32;
	private static final int DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY = 20;
	private static final int SINGLE_LINE_STRINGBUILDER_INITIAL_CAPACITY = 100;
	private static final int MULTI_LINE_STRINGBUILDER_INITIAL_CAPACITY = 1000;

	/**
	 * Read digits from the input until there is a non digit character. The read
	 * digits are appended to the StringBuilder. The first non digit character
	 * is left in the input to be fetched by the next call to
	 * {@code reader.get()}
	 * 
	 * @param digits
	 *            to append the digits to
	 * @param ch
	 *            the first digit already fetched
	 */
	private void processDigits(StringBuilder digits) {
		Integer ch = reader.get();
		while (ch != null && Character.isDigit(ch)) {
			digits.appendCodePoint(ch);
			ch = reader.get();
		}
		reader.pushBack(ch);
	}

	/**
	 * Process the exponent part of a floating point number. The format of such
	 * a part is {@code (E|e)[+|-]digits}
	 * <p>
	 * It leaves the first character not fitting the exponent part in the input
	 * so that the next call to {@code reader.get()} can fetch it.
	 * 
	 * @param exponentCharacters
	 *            buffer to append the characters to
	 * 
	 * @return true if there was an exponent part
	 */
	private boolean processExponent(StringBuilder exponentCharacters) {
		boolean thereWasExponentPart = true;
		Integer ch = reader.get();
		if (ch != null
				&& (ch.equals((Integer) (int) 'e') || ch
						.equals((Integer) (int) 'E'))) {
			Integer expChar = ch; // only to preserve the case: 'E' or 'e'
			ch = reader.get();
			if (ch != null
					&& (ch.equals((Integer) (int) '-') || ch
							.equals((Integer) (int) '+'))) {
				Integer signChar = ch;
				ch = reader.get();
				if (ch != null && Character.isDigit(ch)) {
					exponentCharacters.appendCodePoint(expChar);
					exponentCharacters.appendCodePoint(signChar);
					reader.pushBack(ch);
					processDigits(exponentCharacters);
				} else {
					reader.pushBack(signChar);
					reader.pushBack(expChar);
				}
			} else {// if there is no + or -
				if (ch != null && Character.isDigit(ch)) {
					exponentCharacters.appendCodePoint(expChar);
					reader.pushBack(ch);
					processDigits(exponentCharacters);
				} else {
					reader.pushBack(expChar);
				}
			}
		} else {
			thereWasExponentPart = false;
			reader.pushBack(ch);
		}
		return thereWasExponentPart;
	}

	/**
	 * Read a decimal number from the input. The next character following the
	 * number is left in the input stream to be available for the next
	 * {@code reader.get()}.
	 * <p>
	 * A decimal number does NOT have {@code +} or {@code -} sign, because that
	 * would cause trouble parsing expressions. It contains digits, optionally
	 * decimal point and exponent part following 'e' or 'E'.
	 * <p>
	 * Some languages allow formats like {@code 1.} or {@code .12}. This parser
	 * does not allow such unreadable format. If there is a decimal point then
	 * there has to be a digit on each side.
	 * <p>
	 * The returned lexeme will be double if there is decimal point or exponent
	 * part or both, even if the resulting value could be presented as long.
	 * Thus {@code 1.12E2} is a double number and not a long containing
	 * {@code 112}.
	 * 
	 * @param ch
	 *            one character that was already read from the input
	 * @return the lexical element containing the long or double value
	 */
	private BasicLexicalElement readDecimalNumber() {
		BasicLexicalElement le = BasicLexialElementFactory.create(reader);
		StringBuilder digits = new StringBuilder(
				DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY);
		processDigits(digits);
		Integer ch = reader.get();
		boolean floatFormat;
		if (ch.equals((Integer) (int) '.')) {
			ch = reader.get();
			if (ch != null && Character.isDigit(ch)) {
				floatFormat = true;
				digits.appendCodePoint('.');
				reader.pushBack(ch);
				processDigits(digits);
				processExponent(digits);
			} else {
				floatFormat = false;
				reader.pushBack(ch);
				reader.pushBack((Integer) (int) '.');
			}
		} else {
			reader.pushBack(ch);
			floatFormat = processExponent(digits);
		}
		String s = digits.toString();
		le.setLexeme(s);
		if (floatFormat) {
			le.setType(LexicalElement.TYPE_DOUBLE);
			le.setDoubleValue(Double.parseDouble(s));
		} else {
			le.setType(LexicalElement.TYPE_LONG);
			le.setLongValue(Long.parseLong(s));
		}
		return le;
	}

	private BasicLexicalElement readIdentifier() {
		Integer ch = reader.get();
		BasicLexicalElement le = BasicLexialElementFactory.create(reader,
				LexicalElement.TYPE_IDENTIFIER);
		StringBuilder identifier = new StringBuilder(
				IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY);
		while (ch != null && Character.isUnicodeIdentifierPart(ch)) {
			identifier.appendCodePoint(ch);
			ch = reader.get();
		}
		reader.pushBack(ch);
		String s = identifier.toString();
		le.setLexeme(s);
		le.setStringValue(s);
		return le;
	}

	private boolean isStringTerminated(Integer ch, boolean multiLine)
			throws UnterminatedStringException {
		boolean terminated = false;
		if (ch == null) {
			throw new UnterminatedStringException(reader);
		}
		if (ch.equals((Integer) (int) '"')) {
			if (multiLine) {
				terminated = isTripleQuote();
			} else {
				terminated = true;
			}
		}
		return terminated;
	}

	/**
	 * Convert the character stored in {@code ch} to the character that this
	 * character means in a string when there is a \ before it.
	 * <p>
	 * Very simple n to \n, t to \t and r to \r. Anything else remains itself.
	 * Also " and ' and \.
	 * <p>
	 * Later versions should handle UNICODE escapes reading on from the
	 * {@code reader}. //TODO
	 * 
	 * @param ch
	 *            the character that was following the backslash.
	 * 
	 * @return the converted character.
	 */
	private Integer convertEscapedChar(Integer ch) {
		if (ch != null) {
			switch (ch) {
			case 'n':
				ch = '\n';
				break;
			case 't':
				ch = '\t';
				break;
			case 'r':
				ch = '\r';
				break;
			}
		}
		return ch;
	}

	private void appendSeparator(StringBuilder sb, boolean multiLine) {
		sb.append(multiLine ? "\"\"\"" : "\"");
	}

	/**
	 * Read a string and put it into a lexical element
	 * 
	 * @param stringBufferInitialSize
	 * @param multiLine
	 * @return
	 */
	private BasicLexicalElement readString(int stringBufferInitialSize,
			boolean multiLine) throws UnterminatedStringException {

		StringBuilder string = new StringBuilder(stringBufferInitialSize);
		StringBuilder lexeme = new StringBuilder(stringBufferInitialSize);
		appendSeparator(lexeme, multiLine);

		BasicLexicalElement le = BasicLexialElementFactory.create(reader,
				LexicalElement.TYPE_STRING);

		Integer ch = reader.get();
		while (!isStringTerminated(ch, multiLine)) {
			lexeme.appendCodePoint(ch);
			if (ch.equals((Integer) (int) '\\')) {
				ch = reader.get();
				lexeme.appendCodePoint(ch);
				ch = convertEscapedChar(ch);
			}
			string.appendCodePoint(ch);
			ch = reader.get();
		}
		appendSeparator(lexeme, multiLine);
		le.setLexeme(lexeme.toString());
		le.setStringValue(string.toString());
		le.setType(LexicalElement.TYPE_STRING);
		return le;
	}

	private BasicLexicalElement readSingleLineString()
			throws UnterminatedStringException {
		return readString(SINGLE_LINE_STRINGBUILDER_INITIAL_CAPACITY, false);
	}

	private BasicLexicalElement readMultiLineString()
			throws UnterminatedStringException {
		return readString(MULTI_LINE_STRINGBUILDER_INITIAL_CAPACITY, true);
	}

	private BasicLexicalElement readOneCharacterLexeme() {
		Integer ch = reader.get();
		BasicLexicalElement le = BasicLexialElementFactory.create(reader,
				LexicalElement.TYPE_SYMBOL);
		le.setLexeme(convert(ch));
		le.setType(LexicalElement.TYPE_SYMBOL);
		return le;
	}
}
