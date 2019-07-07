package com.chess4you.gameserver.exceptionHandling.exception;

public class JsonParseError extends Exception {
    public JsonParseError(String lobbyRawDto) {
        super(String.format("The json object is not Valid! %s", lobbyRawDto));
    }
}
