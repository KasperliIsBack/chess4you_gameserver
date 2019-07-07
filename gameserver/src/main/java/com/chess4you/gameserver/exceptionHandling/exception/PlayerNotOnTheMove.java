package com.chess4you.gameserver.exceptionHandling.exception;

public class PlayerNotOnTheMove extends Exception {
    public PlayerNotOnTheMove(String playerUuid) {
        super(String.format("The Player: %s is not on the move!", playerUuid));
    }
}
