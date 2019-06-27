package com.chess4you.gameserver.controller;

import com.chess4you.gameserver.data.ConnectionData;
import com.chess4you.gameserver.data.TurnDto;
import com.chess4you.gameserver.exceptionHandling.exception.InvalidJsonObjectException;
import com.chess4you.gameserver.handler.GameHandler;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api("controller for start a game, get info, get chessboard, get possible turns or do a turn")
public class GameController {

    private final String origin = "http://localhost:4200";
    private GameHandler gameServerHandler;
    private Gson gson;

    @Autowired
    public GameController(GameHandler gameServerHandler) {
        this.gameServerHandler = gameServerHandler;
        this.gson = new Gson();
    }

    @CrossOrigin(origins = origin)
    @PostMapping(value = "/connect")
    @ResponseBody
    @ApiOperation("Connect to Game and register Client")
    String startGame(@ApiParam("The serialized connectionData object. Cannot be null!") @RequestBody String rawCnData) throws Exception {
        ConnectionData cnData;
        try {
            cnData = gson.fromJson(rawCnData, ConnectionData.class);
        } catch (Exception ex){
            throw new InvalidJsonObjectException(rawCnData);
        }
        return gameServerHandler.connect(cnData.getGameDataUuid(), cnData.getPlayerUuid());
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/getInfo")
    @ResponseBody
    @ApiOperation("Get a Info Object by their specific uuid from the lobby")
    String getInfo(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("gameUuid") String gameDataUuid,
                   @ApiParam("The uuid of the player. Cannot be null!") @RequestParam("playerUuid") String playerUuid) throws Exception{
        return gameServerHandler.getInfo(gameDataUuid, playerUuid);
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/getBoard")
    @ResponseBody
    @ApiOperation("Get a specific 2 dimensional Array of the fields for the player by the their uuid from the lobby and player")
    String getBoard(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("gameUuid") String gameDataUuid,
                    @ApiParam("The uuid of the Player. Cannot be null!") @RequestParam("playerUuid") String playerUuid) throws Exception{
        return gameServerHandler.getBoard(gameDataUuid, playerUuid);
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/getTurn")
    @ResponseBody
    @ApiOperation("Get a List of possible movements by the uuid from the lobby and Player and the Position")
    String getTurn(@ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("gameUuid") String gameDataUuid,
                   @ApiParam("The uuid of the lobby. Cannot be null!") @RequestParam("playerUuid") String playerUuid,
                   @ApiParam("The serialized Position Object. Cannot be null!") @RequestParam("position")String position) throws Exception{
        return gameServerHandler.getTurn(gameDataUuid, playerUuid, position);
    }

    @CrossOrigin(origins = origin)
    @PostMapping(value = "/doTurn")
    @ResponseBody
    @ApiOperation("Do the Turn from his TurnData")
    String doTurn(@ApiParam("The serialized doTurn Object. Cannot be null!") @RequestBody String rawTurnDto) throws Exception {
        TurnDto turnDto;
        try {
            turnDto = gson.fromJson(rawTurnDto, TurnDto.class);
        } catch (Exception ex) {
            throw new InvalidJsonObjectException(rawTurnDto);
        }
        return gameServerHandler.doTurn(turnDto.getCnData().getGameDataUuid(), turnDto.getCnData().getPlayerUuid(), turnDto.getMovement());
    }
}