package com.cgev.matcher.exception;

public class MatcherException extends RuntimeException {
    public MatcherException() {
        //Localize message
        super("General exception occurred");
    }

    public MatcherException(String message) {
        //Localize message
        super(message);
    }
}
