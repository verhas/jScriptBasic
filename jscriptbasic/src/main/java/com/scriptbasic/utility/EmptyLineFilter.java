package com.scriptbasic.utility;

public class EmptyLineFilter implements CommentFilter {

    @Override
    public Boolean isComment(final String line) {
        return line.length() == 0;
    }

}
