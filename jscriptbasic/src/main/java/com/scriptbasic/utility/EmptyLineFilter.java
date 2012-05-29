package com.scriptbasic.utility;

public class EmptyLineFilter implements CommentFilter {

    @Override
    public Boolean isComment(String line) {
        return line.length() == 0;
    }

}
