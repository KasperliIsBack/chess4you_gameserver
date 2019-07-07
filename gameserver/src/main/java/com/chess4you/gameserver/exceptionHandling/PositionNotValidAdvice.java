package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.PositionNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PositionNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(PositionNotValid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String NotValidPositionForPieceAdvice(PositionNotValid ex){
        return ex.getMessage();
    }
}
