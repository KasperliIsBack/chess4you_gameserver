package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.SequenceOfCommandsIsNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SequenceOfCommandsIsNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(SequenceOfCommandsIsNotValid.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String SequenceOfCommandsIsNotValidAdvice(SequenceOfCommandsIsNotValid ex){
        return ex.getMessage();
    }
}
