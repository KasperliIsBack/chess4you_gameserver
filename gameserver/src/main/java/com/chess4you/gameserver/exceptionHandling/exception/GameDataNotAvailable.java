package com.chess4you.gameserver.exceptionHandling.exception;

public class GameDataNotAvailable extends Exception {
    public GameDataNotAvailable(String gameDataUuid) {
        super(String.format("The GameData is not Valid! %s", gameDataUuid));
    }
}
