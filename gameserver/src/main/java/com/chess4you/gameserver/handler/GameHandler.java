package com.chess4you.gameserver.handler;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.data.board.Field;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.service.GameService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameHandler {

    private GameService gameService;
    private Gson gson;

    @Autowired
    public GameHandler(GameService gameDataService){
        this.gameService = gameDataService;
        this.gson = new Gson();
    }

    public String connect(String gameUuid, String playerUuid){
        String message = gameService.connect(gameUuid, playerUuid);
        return message;
    }

    public String getInfo(String gameUuid, String playerUuid) {
        GameData gameData = gameService.getInfo(gameUuid, playerUuid);
        return gson.toJson(gameData);
    }

    public String getBoard(String gameUuid, String playerUuid) {
        Field[][] board = gameService.getBoard(gameUuid, playerUuid);
        return gson.toJson(board);
    }

    public String getTurn(String gameUuid, String playerUuid, String position) {
        Movement[] movementArray = gameService.getTurn(gameUuid, playerUuid, position);
        return gson.toJson(movementArray);
    }

    public String doTurn(String lobbyUuid, String playerUuid, String movement) {
        Field[][] board = gameService.doTurn(lobbyUuid, playerUuid, movement);
        return gson.toJson(board);
    }
}
