package com.cgev.matcher.exception;

public class CSVParseException extends RuntimeException{
    public CSVParseException() {
        //Localize message
        super("Unable to parse csv file");
    }
}
