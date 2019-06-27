package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.PlayerIsNotValid;
import com.chess4you.gameserver.exceptionHandling.exception.SequenceOfCommandsIsNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerIsNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(PlayerIsNotValid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String PlayerIsNotValidAdvice(PlayerIsNotValid ex){
        return ex.getMessage();
    }
}
