package com.chess4you.gameserver.exceptionHandling.exception;

public class PositionNotValid extends Exception {
    public PositionNotValid(String position) {
        super(String.format("The position for the piece is not valid! %s", position));
    }
}
