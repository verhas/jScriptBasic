package com.scriptbasic.utility;

public class CharacterCheck {
	public static boolean isNewLine(Integer ch) {
		if (ch != null) {
			int type = Character.getType(ch);
			return type == Character.LINE_SEPARATOR
					|| type == Character.PARAGRAPH_SEPARATOR
					|| ch.equals((Integer) (int) '\n');
		} else {
			return false;
		}
	}
}
