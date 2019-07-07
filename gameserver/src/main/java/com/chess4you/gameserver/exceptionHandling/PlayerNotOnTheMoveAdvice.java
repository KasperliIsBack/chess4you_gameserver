package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.PlayerNotOnTheMove;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerNotOnTheMoveAdvice {
    @ResponseBody
    @ExceptionHandler(PlayerNotOnTheMove.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String PlayerNotOnTheMoveAdvice(PlayerNotOnTheMove ex){
        return ex.getMessage();
    }
}
