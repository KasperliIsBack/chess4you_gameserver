package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.InvalidJsonObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidJsonObjectAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidJsonObjectException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String InvaldJsonObjectHandler(InvalidJsonObjectException ex) {
     return  ex.getMessage();
    }
}
