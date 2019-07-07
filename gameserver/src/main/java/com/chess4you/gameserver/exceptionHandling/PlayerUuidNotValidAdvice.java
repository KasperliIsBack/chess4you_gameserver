package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.PlayerUuidNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerUuidNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(PlayerUuidNotValid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String PlayerIsNotValidAdvice(PlayerUuidNotValid ex){
        return ex.getMessage();
    }
}
