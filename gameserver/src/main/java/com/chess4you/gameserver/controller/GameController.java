package com.chess4you.gameserver.controller;


import com.chess4you.gameserver.handler.GameHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@Api(description = "controller for start a game, get info, get chessboard, get possible turns or do a turn")
public class GameController {

    private GameHandler gameServerHandler;


    @Autowired
    public GameController(GameHandler gameServerHandler) {
        this.gameServerHandler = gameServerHandler;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/connect", consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    @ApiOperation("Connect to Game and register Client")
    String startGame(@ApiParam("The form for the connection Cannot be null!") @RequestParam Map<String, String> formData) {
        if(isConnectFormValid(formData)) {
            return gameServerHandler.connect(formData.get("lobbyUuid"),
                    formData.get("playerUuid"));
        } {
            // Todo throw Exception
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getInfo/")
    @ResponseBody
    @ApiOperation("Get a Info Object by their specific uuid from the lobby")
    String getInfo(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("lobbyUuid") String lobbyUuid,
                   @ApiParam("The uuid of the player. Cannot be null!") @RequestParam("playerUuid") String playerUuid){
        return gameServerHandler.getInfo(lobbyUuid, playerUuid);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getBoard")
    @ResponseBody
    @ApiOperation("Get a specific 2 dimensional Array of the fields for the player by the their uuid from the lobby and player")
    String getBoard(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("lobbyUuid") String lobbyUuid,
                    @ApiParam("The uuid of the Player. Cannot be null!") @RequestParam("playerUuid") String playerUuid){
        return gameServerHandler.getBoard(lobbyUuid, playerUuid);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getTurn")
    @ResponseBody
    @ApiOperation("Get a List of possible movements by the uuid from the lobby and Player and the Position")
    String getTurn(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("lobbyUuid") String lobbyUuid,
                   @ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("playerUuid") String playerUuid,
                   @ApiParam("The serialized Position Object. Cannot be null!") @RequestParam("position")String position){
        return gameServerHandler.getTurn(lobbyUuid, playerUuid, position);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/doTurn", consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    @ApiOperation("Do the Turn from his TurnData")
    String doTurn(@ApiParam("The form of the turn. Cannot be null!") @RequestParam Map<String, String> formData) {
        if(isDoTurnFormValid(formData)) {
            return gameServerHandler.doTurn(formData.get("lobbyUuid"), formData.get("playerUuid"), formData.get("movement"));
        } else {
            // Todo throw Exception
            return null;
        }
    }

    private boolean isDoTurnFormValid(Map<String, String> formData) {
        if(formData.size() == 3
        && formData.containsKey("lobbyUuid")
        && formData.containsKey("playerUuid")
        && formData.containsKey("movement")) {
            for(var entry : formData.values()) {
                if(entry.isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isConnectFormValid(Map<String, String> formData) {
        if(formData.size() == 2
                && formData.containsKey("lobbyUuid")
                && formData.containsKey("playerUuid")){
            for(var entry : formData.values()) {
                if(entry.isEmpty()){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}