package com.chess4you.gameserver.exceptionHandling;

import com.chess4you.gameserver.exceptionHandling.exception.NotValidPositionForPiece;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotValidPositionForPieceAdvice {
    @ResponseBody
    @ExceptionHandler(NotValidPositionForPiece.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String NotValidPositionForPieceAdvice(NotValidPositionForPiece ex){
        return ex.getMessage();
    }
}
