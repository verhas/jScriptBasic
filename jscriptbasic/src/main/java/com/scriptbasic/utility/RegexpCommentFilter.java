package com.scriptbasic.utility;

import java.util.regex.Pattern;

public class RegexpCommentFilter implements CommentFilter {

    private String regexp;

    public RegexpCommentFilter(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public Boolean isComment(String line) {
        return Pattern.matches(regexp, line);
    }

}
