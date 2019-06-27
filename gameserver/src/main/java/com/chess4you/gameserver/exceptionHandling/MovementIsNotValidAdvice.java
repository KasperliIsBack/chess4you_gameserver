package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.MovementIsNotValid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MovementIsNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(MovementIsNotValid.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String MovementIsNotValidAdvice(MovementIsNotValid ex){
        return ex.getMessage();
    }
}
