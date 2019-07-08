package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.game.GameData;
import com.chess4you.gameserver.data.board.Field;
import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import com.chess4you.gameserver.exceptionHandling.exception.*;
import com.google.gson.Gson;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class GameService {

    private TurnService turnService;
    private GameDataService gameDataService;
    private GameServerService gameServerService;
    private GameData gameData;
    private Movement[] currentMovementArray;
    private final int GamePeriodInMinute = 10;
    @Autowired
    public GameService(TurnService turnService, GameDataService gameDataService, GameServerService gameServerService){
        this.turnService = turnService;
        this.gameDataService = gameDataService;
        this.gameServerService = gameServerService;
    }

    public String connect(String gameDataUuid, String playerUuid) throws Exception {
        gameData = getGameData(gameDataUuid);
        gameData = setIsPlayerConnected(gameData, playerUuid);
        gameData = startGameIfAllPlayersConnected(gameData);
        gameDataService.updateGameData(gameData);
        return "Connection was successful";
    }

    public GameData getInfo(String gameDataUuid) throws Exception {
        gameData = getGameData(gameDataUuid);
        return gameData;
    }

    public Field[][] getBoard(String gameDataUuid, String playerUuid) throws Exception {
        gameData = getGameData(gameDataUuid);
        return generateBoard(gameData, playerUuid);
    }

    public Movement[] getTurn(String gameDataUuid, String playerUuid, String position) throws Exception {
        gameData = getGameData(gameDataUuid);
        if(isPlayerOnTurn(gameData, playerUuid)) {
            Map<Position, Piece> mapPosPiece = new HashMap<>();
            for (var tmpPiece : gameData.getMapPosPiece().values()) {
                mapPosPiece.put(tmpPiece.getPosition(), tmpPiece);
            }
            var piece = getPieceOnThatPosition(gameData, position);
            currentMovementArray = turnService.getPossibleTurnFor(mapPosPiece, piece, setIsReverse(gameData, playerUuid));
            if(IsInGamePeriod(gameData)) {
                return currentMovementArray;
            } else {
                return currentMovementArray;
            }
        } else {
            throw new PlayerUuidNotValid(playerUuid);
        }
    }

    public Field[][] doTurn(String gameDataUuid, String playerUuid, Movement movement) throws Exception {
        gameData = getGameData(gameDataUuid);
        if(isPlayerOnTurn(gameData, playerUuid)) {
            if (currentMovementArray != null) {
                if (Arrays.asList(currentMovementArray).contains(movement)) {
                    gameData = turnService.doTurn(gameData, movement);
                    Field[][] board = generateBoard(gameData, playerUuid);
                    if(IsInGamePeriod(gameData)) {
                        gameData = updateTurn(gameData, playerUuid);
                        gameDataService.updateGameData(gameData);
                        return board;
                    } else {
                        gameData = updateTurn(gameData, playerUuid);
                        gameDataService.updateGameData(gameData);
                        return board;
                    }
                } else {
                    throw new MovementIsNotValid(movement);
                }
            } else {
                throw new SequenceOfCommandsNotValid();
            }
        } else {
            throw new PlayerNotOnTheMove(playerUuid);
        }
    }

    private boolean IsInGamePeriod(GameData gameData) {
        Date startDate = gameData.getTurnDate();
        Date currentDate  = new Date();
        long difference = startDate.getTime() - currentDate.getTime();
        return difference < ((long) GamePeriodInMinute / 60000);
    }

    private GameData updateTurn(GameData gameData, String playerUuid) {
        if(gameData.getFirstPlayer().getPlayerUuid().equals(playerUuid)) {
            gameData.setCurrentPlayer(gameData.getSecondPlayer());
        } else {
            gameData.setCurrentPlayer(gameData.getFirstPlayer());
        }
        gameData.setTurnDate(new Date());
        return gameData;
    }

    private Piece getPieceOnThatPosition(GameData gameData, String position) throws PositionNotValid {
        var mapPosPiece = gameData.getMapPosPiece();
        var piece = mapPosPiece.get(position);
        if(piece != null) {
            return piece;
        } else {
            throw new PositionNotValid(new Gson().toJson(position));
        }
    }

    private GameData startGameIfAllPlayersConnected(GameData gameData) throws IOException, URISyntaxException {
        if(gameData.isFirstPlayerConnected() && gameData.isSecondPlayerConnected()) {
            var dicPosPiece = generateMapPosPiece();
            gameData.setMapPosPiece(dicPosPiece);
            gameData.setTurnDate(new Date());
            gameData.setGamePeriodInMinute(GamePeriodInMinute);
        }
        return gameData;
    }

    private Field[][] generateBoard(GameData gameData, String playerUuid) {
        boolean reverse = setIsReverse(gameData, playerUuid);
        int boardSize = 8;
        Map<String, Piece> mapPosPiece = gameData.getMapPosPiece();

        Field[][] chessBoard = new Field[boardSize][boardSize];
        for (int PosY = 0; PosY < boardSize; PosY++) {
            for (int PosX = 0; PosX < boardSize; PosX++) {
                chessBoard[PosY][PosX] = new Field(false);
            }
        }
        List<String> posList = new ArrayList<>(mapPosPiece.keySet());
        if(reverse) {
            for(var tmpPosition : posList) {
                var position = mapPosPiece.get(tmpPosition).getPosition();
                chessBoard[7 - position.getPosY()][7 -  position.getPosX()] = new Field(mapPosPiece.get(tmpPosition), true);
            }
        } else {
            for(var tmpPosition : posList) {
                var position = mapPosPiece.get(tmpPosition).getPosition();
                chessBoard[position.getPosY()][ position.getPosX()] = new Field(mapPosPiece.get(tmpPosition), true);
            }
        }
        return chessBoard;
    }


    private boolean setIsReverse(GameData gameData, String playerUuid) {
        boolean reverse;
        Color color;
        if(gameData.getFirstPlayer().getPlayerUuid().trim().equals(playerUuid)) {
            color = gameData.getColorFirstPlayer();
        } else {
            color = gameData.getColorSecondPlayer();
        }
        reverse = color == Color.White;
        return reverse;
    }

    private Piece getNewPiece(Color color, PieceType pieceType, Position position) throws URISyntaxException, IOException {
        String filename = String.format("%s.json", pieceType.name());
        URI uri = getClass().getClassLoader().getResource(filename).toURI();
        Path path = Paths.get(uri);
        String rawData = new String(Files.readAllBytes(path));
        Direction[] directions = new Gson().fromJson(rawData, Direction[].class);
        return new Piece(pieceType, directions, color, position);
    }

    private Map<String, Piece> generateMapPosPiece() throws IOException, URISyntaxException {
        var gson = new Gson();
        Map<String, Piece> mapPosPiece = new Hashtable<>();
        PieceType[] listPieceType = { PieceType.Rock, PieceType.Knight, PieceType.Bishop, PieceType.King, PieceType.Queen, PieceType.Bishop, PieceType.Knight, PieceType.Rock };
        for (int PosY = 0; PosY < 8; PosY++) {
            for (int PosX = 0; PosX < 8; PosX++) {
                if(PosY == 0) {
                    mapPosPiece.put(gson.toJson(new Position(PosX, PosY)), getNewPiece(Color.Black, listPieceType[PosX], new Position(PosX, PosY)));
                } else if(PosY == 1) {
                    mapPosPiece.put(gson.toJson(new Position(PosX, PosY)), getNewPiece(Color.Black, PieceType.Pawn, new Position(PosX, PosY)));
                } else if(PosY == 6) {
                    mapPosPiece.put(gson.toJson(new Position(PosX, PosY)), getNewPiece(Color.White, PieceType.Pawn, new Position(PosX, PosY)));
                } else if(PosY == 7) {
                    mapPosPiece.put(gson.toJson(new Position(PosX, PosY)), getNewPiece(Color.White, listPieceType[PosX], new Position(PosX, PosY)));
                }
            }
        }
        return mapPosPiece;
    }

    private GameData setIsPlayerConnected(GameData gameData, String playerUuid) {
        if(gameData.getFirstPlayer().getPlayerUuid().trim().equals(playerUuid)) {
            gameData.setFirstPlayerConnected(true);
        } else {
            gameData.setSecondPlayerConnected(true);
        }
        return gameData;
    }

    private boolean isPlayerOnTurn(GameData gameData, String currentPlayerUuid) {
        return gameData.getCurrentPlayer().getPlayerUuid().equals(currentPlayerUuid);
    }

    public void registerGameServer() {
        gameServerService.registerGameServer();
    }

    private GameData getGameData(String gameDataUuid) throws GameDataNotAvailable {
        GameData tmpGameData;
        try {
            tmpGameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex){
            throw new GameDataNotAvailable(gameDataUuid);
        }
        return tmpGameData;
    }
}
