package com.scriptbasic.interfaces;

public interface HierarchicalReader extends Reader {
	public void include(Reader reader);
}
