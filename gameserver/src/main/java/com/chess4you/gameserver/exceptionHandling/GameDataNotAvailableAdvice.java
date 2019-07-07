package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.GameDataNotAvailable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GameDataNotAvailableAdvice {
    @ResponseBody
    @ExceptionHandler(GameDataNotAvailable.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String GameDataIsNotAvailableAdvice(GameDataNotAvailable ex){
        return ex.getMessage();
    }
}
