package com.cgev.matcher.exception;

public class CSVParseException extends MatcherException{
    public CSVParseException() {
        //Localize message
        super("Unable to parse csv file");
    }
}
