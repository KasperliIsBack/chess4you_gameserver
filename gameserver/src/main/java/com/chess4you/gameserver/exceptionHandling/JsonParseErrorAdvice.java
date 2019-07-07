package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.JsonParseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JsonParseErrorAdvice {
    @ResponseBody
    @ExceptionHandler(JsonParseError.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String InvaldJsonObjectHandler(JsonParseError ex) {
     return  ex.getMessage();
    }
}
