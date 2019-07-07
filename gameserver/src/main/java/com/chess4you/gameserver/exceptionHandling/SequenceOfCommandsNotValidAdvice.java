package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.SequenceOfCommandsNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SequenceOfCommandsNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(SequenceOfCommandsNotValid.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String SequenceOfCommandsIsNotValidAdvice(SequenceOfCommandsNotValid ex){
        return ex.getMessage();
    }
}
