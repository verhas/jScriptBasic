package com.scriptbasic.utility;

import java.util.regex.Pattern;

public class RegexpCommentFilter implements CommentFilter {

    private final String regexp;

    public RegexpCommentFilter(final String regexp) {
        this.regexp = regexp;
    }

    @Override
    public Boolean isComment(final String line) {
        return Pattern.matches(regexp, line);
    }

}
