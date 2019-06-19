package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.data.board.Field;
import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        this.gameDataService = gameDataService;
    }

    public String connect(String gameUuid, String playerUuid) {
        gameData = gameDataService.getGameData(gameUuid, playerUuid);
        gameData = setIsPlayerConnected(gameData, playerUuid);
        gameData = startGameIfAllPlayersConnected(gameData);
        gameDataService.updateGameData(gameData);
        return "Connection was successful";
    }

    public GameData getInfo(String gameUuid, String playerUuid) {
        return gameDataService.getGameData(gameUuid, playerUuid);
    }

    public Field[][] getBoard(String gameUuid, String playerUuid) {
        gameData = gameDataService.getGameData(gameUuid, playerUuid);
        return generateBoard(gameData, playerUuid);
    }

    public Movement[] getTurn(String gameUuid, String playerUuid, String position) {
        gameData = gameDataService.getGameData(gameUuid, playerUuid);
        currentMovementArray = turnService.getPossibleTurnFor(gameData.getDicPosPiece(), getPieceOnThatPosition(gameData, position));
        if(IsInGamePeriod(gameData)) {
            return currentMovementArray;
        } else {
            // Todo send message
            return currentMovementArray;
        }
    }

    public Field[][] doTurn(String gameUuid, String playerUuid, Movement movement) {
        gameData = gameDataService.getGameData(gameUuid, playerUuid);
        if (currentMovementArray != null) {
            if (Arrays.asList(currentMovementArray).contains(movement)) {
                gameData = turnService.doTurn(gameData, movement);
                Field[][] board = generateBoard(gameData, playerUuid);
                if(IsInGamePeriod(gameData)) {
                    gameData = updateTurn(gameData, playerUuid);
                    gameDataService.updateGameData(gameData);
                    return board;
                } else {
                    // Todo send message
                    gameData = updateTurn(gameData, playerUuid);
                    gameDataService.updateGameData(gameData);
                    return board;
                }
            } else {
                // Todo throw Exception movement is not valid
                return null;
            }
        } else {
            // Todo throw Exception not valid sequence of commands
            return null;
        }
    }

    private boolean IsInGamePeriod(GameData gameData) {
        Date startDate = gameData.getTurnDate();
        Date currentDate  = new Date();
        long difference = startDate.getTime() - currentDate.getTime();
        return difference < ((long) GamePeriodInMinute / 60000);
    }

    private GameData updateTurn(GameData gameData, String playerUuid) {
        if(gameData.getFirstPlayer().getPlayerUuid() == playerUuid) {
            gameData.setCurrentPlayer(gameData.getSecondPlayer());
        } else {
            gameData.setCurrentPlayer(gameData.getFirstPlayer());
        }
        gameData.setTurnDate(new Date());
        return gameData;
    }

    private Piece getPieceOnThatPosition(GameData gameData, String position) {
        Piece piece = gameData.getDicPosPiece().get(position);
        if(piece != null) {
            return piece;
        } else {
            // Todo throw Exception not valid position for a piece
            return null;
        }
    }

    private GameData startGameIfAllPlayersConnected(GameData gameData) {
        if(gameData.isFirstPlayerConnected() && gameData.isSecondPlayerConnected()) {
            var dicPosPiece = generateDicPosPiece();
            gameData.setDicPosPiece(dicPosPiece);
            gameData.setTurnDate(new Date());
            gameData.setGamePeriodInMinute(GamePeriodInMinute);
        }
        return gameData;
    }

    private Field[][] generateBoard(GameData gameData, String playerUuid) {
        boolean reverse = setIsReverse(gameData, playerUuid);
        int boardSize = 8;
        Dictionary<Position, Piece> dicPosPiece = gameData.getDicPosPiece();

        Field[][] chessBoard = new Field[boardSize][boardSize];
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                chessBoard[y][x] = new Field(false);
            }
        }
        if(reverse) {
            var reverseList = Collections.list(dicPosPiece.keys());
            Collections.reverse(reverseList);
            for(var entry : reverseList) {
                chessBoard[entry.getPosY()][ entry.getPosX()] = new Field(dicPosPiece.get(entry), true);
            }
        } else {
            for (var entry : Collections.list(dicPosPiece.keys())) {
                chessBoard[entry.getPosY()][entry.getPosX()] = new Field(dicPosPiece.get(entry), true);
            }
        }
        return chessBoard;
    }

    private boolean setIsReverse(GameData gameData, String playerUuid) {
        boolean reverse;
        Color color;
        if(gameData.getFirstPlayer().getPlayerUuid() == playerUuid) {
            color = gameData.getColorFirstPlayer();
        } else {
            color = gameData.getColorSecondPlayer();
        }
        reverse = color == Color.White;
        return reverse;
    }

    private Dictionary<Position, Piece> generateDicPosPiece() {
        // Todo create a smaller generate with reverse or so
        Dictionary<Position, Piece> dicPosPiece = new Hashtable<>();
        dicPosPiece.put(new Position(0,0), new Rock(Color.Black));
        dicPosPiece.put(new Position(1,0), new Knight(Color.Black));
        dicPosPiece.put(new Position(2,0), new Bishop(Color.Black));
        dicPosPiece.put(new Position(3,0), new King(Color.Black));
        dicPosPiece.put(new Position(4,0), new Queen(Color.Black));
        dicPosPiece.put(new Position(5,0), new Bishop(Color.Black));
        dicPosPiece.put(new Position(6,0), new Knight(Color.Black));
        dicPosPiece.put(new Position(7,0), new Rock(Color.Black));
        dicPosPiece.put(new Position(0,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(1,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(2,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(3,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(4,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(5,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(6,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(7,1), new Pawn(Color.Black));
        dicPosPiece.put(new Position(0,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(1,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(2,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(3,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(4,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(5,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(6,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(7,6), new Pawn(Color.White));
        dicPosPiece.put(new Position(0,7), new Rock(Color.White));
        dicPosPiece.put(new Position(1,7), new Knight(Color.White));
        dicPosPiece.put(new Position(2,7), new Bishop(Color.White));
        dicPosPiece.put(new Position(3,7), new Queen(Color.White));
        dicPosPiece.put(new Position(4,7), new King(Color.White));
        dicPosPiece.put(new Position(5,7), new Bishop(Color.White));
        dicPosPiece.put(new Position(6,7), new Knight(Color.White));
        dicPosPiece.put(new Position(7,7), new Rock(Color.White));
        return dicPosPiece;
    }

    private GameData setIsPlayerConnected(GameData gameData, String playerUuid) {
        if(gameData.getFirstPlayer().getPlayerUuid() == playerUuid) {
            gameData.setFirstPlayerConnected(true);
        } else {
            gameData.setSecondPlayerConnected(true);
        }
        return gameData;
    }

    public void registerGameServer() {
        gameServerService.registerGameServer();
    }
}
