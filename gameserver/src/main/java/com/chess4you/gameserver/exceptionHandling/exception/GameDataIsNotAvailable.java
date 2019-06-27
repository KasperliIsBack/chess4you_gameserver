package com.chess4you.gameserver.exceptionHandling.exception;

public class GameDataIsNotAvailable extends Exception {
    public GameDataIsNotAvailable(String gameDataUuid) {
        super(String.format("The GameData is not Valid! %s", gameDataUuid));
    }
}
