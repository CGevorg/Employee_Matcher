package com.cgev.matcher.exception.handler;

import com.cgev.matcher.exception.CouldNotParseFileException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(CouldNotParseFileException.class)
    String onResourceNotFoundHandler(CouldNotParseFileException ex) {
        //Return localized message
        return ex.getMessage();
    }
}
