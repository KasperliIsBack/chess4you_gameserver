package com.chess4you.gameserver.exceptionHandling.exception;

public class InvalidJsonObjectException extends Exception {
    public InvalidJsonObjectException(String lobbyRawDto) {
        super(String.format("The json object is not Valid! %s", lobbyRawDto));
    }
}
