package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.GameDataIsNotAvailable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GameDataIsNotAvailableAdvice {
    @ResponseBody
    @ExceptionHandler(GameDataIsNotAvailable.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String GameDataIsNotAvailableAdvice(GameDataIsNotAvailable ex){
        return ex.getMessage();
    }
}
