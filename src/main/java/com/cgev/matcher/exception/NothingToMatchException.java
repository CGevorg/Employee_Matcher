package com.cgev.matcher.exception;

public class NothingToMatchException extends MatcherException {
    public NothingToMatchException() {
        //Localize message
        super("There is nothing to match");
    }

}
