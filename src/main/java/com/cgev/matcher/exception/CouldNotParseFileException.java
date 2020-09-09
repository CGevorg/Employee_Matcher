package com.cgev.matcher.exception;

public class CouldNotParseFileException extends MatcherException {
    public CouldNotParseFileException(String extension) {
        super("Could not parse file with given extension: " + extension);
    }
}
