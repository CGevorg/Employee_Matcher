package com.cgev.matcher.exception;

public class NothingToMatchException extends RuntimeException {
    public NothingToMatchException() {
        //Localize message
        super("There is nothing to match");
    }

}
