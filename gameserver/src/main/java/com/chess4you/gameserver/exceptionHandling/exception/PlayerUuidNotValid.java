package com.chess4you.gameserver.exceptionHandling.exception;

public class PlayerUuidNotValid extends Exception {
    public PlayerUuidNotValid(String playerUuid) {
        super(String.format("The PlayerUuid is not Valid! %s", playerUuid));
    }
}
