package com.chess4you.gameserver.exceptionHandling.exception;

public class NotValidPositionForPiece extends Exception {
    public NotValidPositionForPiece(String position) {
        super(String.format("The position for the piece is not valid! %s", position));
    }
}
