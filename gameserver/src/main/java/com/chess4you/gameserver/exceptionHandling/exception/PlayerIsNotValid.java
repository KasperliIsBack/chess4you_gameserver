package com.chess4you.gameserver.exceptionHandling.exception;

public class PlayerIsNotValid extends Exception {
    public PlayerIsNotValid(String playerUuid) {
        super(String.format("The PlayerUuid is not Valid! %s", playerUuid));
    }
}
