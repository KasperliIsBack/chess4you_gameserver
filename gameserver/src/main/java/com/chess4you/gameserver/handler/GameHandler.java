package com.chess4you.gameserver.handler;

import com.chess4you.gameserver.data.game.GameData;
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
        this.gameService.registerGameServer();
    }

    public String connect(String gameDataUuid, String playerUuid) throws Exception {
        return gameService.connect(gameDataUuid, playerUuid);
    }

    public String getInfo(String gameDataUuid) throws Exception {
        GameData gameData = gameService.getInfo(gameDataUuid);
        return gson.toJson(gameData);
    }

    public String getBoard(String gameDataUuid, String playerUuid) throws Exception {
        Field[][] board = gameService.getBoard(gameDataUuid, playerUuid);
        return gson.toJson(board);
    }

    public String getTurn(String gameDataUuid, String playerUuid, String position) throws Exception {
        Movement[] movementArray = gameService.getTurn(gameDataUuid, playerUuid, position);
        return gson.toJson(movementArray);
    }

    public String doTurn(String gameDataUuid, String playerUuid, Movement movement) throws Exception {
        Field[][] board = gameService.doTurn(gameDataUuid, playerUuid, movement);
        return gson.toJson(board);
    }
}
