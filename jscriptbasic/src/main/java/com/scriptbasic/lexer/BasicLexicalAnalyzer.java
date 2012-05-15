package com.scriptbasic.lexer;

import java.util.Deque;
import java.util.LinkedList;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.Reader;

public class BasicLexicalAnalyzer implements LexicalAnalyzer {
	private Reader reader;

	public void set(Reader reader) {
		this.reader = reader;
	}

	Deque<LexicalElement> lexicalElementQueue = new LinkedList<LexicalElement>();

	public LexicalElement get() {
		if (lexicalElementQueue.isEmpty()) {
			readTheNextLine();
		}
		LexicalElement le = null;
		try {
			le = lexicalElementQueue.removeFirst();
		} finally {
		}
		return le;
	}

	private void readTheNextLine() {
		Boolean lineEndFound = false;
		for (Integer ch = reader.get(); ch != null && !lineEndFound; ch = reader
				.get()) {
			BasicLexicalElement le = null;
			int type = Character.getType(ch);
			if (type == Character.LINE_SEPARATOR
					|| type == Character.PARAGRAPH_SEPARATOR) {
				le = readOneCharacterLexeme(ch);
				lineEndFound = true;
			}
			if (Character.isUnicodeIdentifierStart(ch)) {

			}
			if (ch.equals('"')) {
				Integer second = reader.get();
				Integer third = reader.get();
				if (second.equals('"') && third.equals('"')) {
					le = readMultiLineString();
				} else {
					reader.pushBack(third);
					reader.pushBack(second);
					le = readSingleLineString();
				}
			}
			if (le != null) {
				lexicalElementQueue.add(le);
			}
		}
	}

	private BasicLexicalElement readSingleLineString(){
		//TODO
		return null;
	}
	private BasicLexicalElement readMultiLineString(){
		//TODO
		return null;
	}
	private BasicLexicalElement readOneCharacterLexeme(Integer ch) {
		BasicLexicalElement le = new BasicLexicalElement();
		le.setFileName(reader.fileName());
		le.setLineNumber(reader.lineNumber());
		le.setLexeme(ch.toString());
		return le;
	}
}
