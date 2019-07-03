package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.data.board.Field;
import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.*;
import com.chess4you.gameserver.exceptionHandling.exception.*;
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
        this.gameServerService = gameServerService;
    }

    public String connect(String gameDataUuid, String playerUuid) throws Exception {
        try {
            gameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex) {
            throw new GameDataIsNotAvailable(gameDataUuid);
        }
        gameData = setIsPlayerConnected(gameData, playerUuid);
        gameData = startGameIfAllPlayersConnected(gameData);
        gameDataService.updateGameData(gameData);
        return "Connection was successful";
    }

    public GameData getInfo(String gameDataUuid, String playerUuid) throws Exception {
        try {
            gameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex) {
            throw new GameDataIsNotAvailable(gameDataUuid);
        }
        return gameData;
    }

    public Field[][] getBoard(String gameDataUuid, String playerUuid) throws Exception {
        try {
            gameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex) {
            throw new GameDataIsNotAvailable(gameDataUuid);
        }
        return generateBoard(gameData, playerUuid);
    }

    public Movement[] getTurn(String gameDataUuid, String playerUuid, String position) throws Exception {
        try {
            gameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex) {
            throw new GameDataIsNotAvailable(gameDataUuid);
        }
        if(isPlayerOnTurn(gameData, playerUuid)) {
            currentMovementArray = turnService.getPossibleTurnFor(gameData.getMapPosPiece(), getPieceOnThatPosition(gameData, position));
            if(IsInGamePeriod(gameData)) {
                return currentMovementArray;
            } else {
                return currentMovementArray;
            }
        } else {
            throw new PlayerIsNotValid(playerUuid);
        }
    }

    public Field[][] doTurn(String gameDataUuid, String playerUuid, Movement movement) throws Exception {
        try {
            gameData = gameDataService.getGameData(gameDataUuid);
        } catch (Exception ex) {
            throw new GameDataIsNotAvailable(gameDataUuid);
        }
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
                throw new SequenceOfCommandsIsNotValid();
            }
        } else {
            throw new PlayerIsNotValid(playerUuid);
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

    private Piece getPieceOnThatPosition(GameData gameData, String position) throws NotValidPositionForPiece {
        Piece piece = gameData.getMapPosPiece().get(position);
        if(piece != null) {
            return piece;
        } else {
            throw new NotValidPositionForPiece(position);
        }
    }

    private GameData startGameIfAllPlayersConnected(GameData gameData) {
        if(gameData.isFirstPlayerConnected() && gameData.isSecondPlayerConnected()) {
            var dicPosPiece = generateMapPosPiece();
            gameData.setMapPosPiece(dicPosPiece);
            gameData.setTurnDate(new Date());
            gameData.setGamePeriodInMinute(GamePeriodInMinute);
        }
        return gameData;
    }

    public Field[][] generateBoard(GameData gameData, String playerUuid) {
        boolean reverse = setIsReverse(gameData, playerUuid);
        int boardSize = 8;
        Map<Position, Piece> mapPosPiece = gameData.getMapPosPiece();

        Field[][] chessBoard = new Field[boardSize][boardSize];
        for (int PosY = 0; PosY < boardSize; PosY++) {
            for (int PosX = 0; PosX < boardSize; PosX++) {
                chessBoard[PosY][PosX] = new Field(false);
            }
        }
        if(reverse) {
            List<Position> reversePositionList = new ArrayList<>();
            reversePositionList.addAll(mapPosPiece.keySet());
            Collections.reverse(reversePositionList);
            for(var position : reversePositionList) {
                chessBoard[position.getPosY()][ position.getPosX()] = new Field(mapPosPiece.get(position), true);
            }
        } else {
            List<Position> positionList = new ArrayList<>();
            positionList.addAll(mapPosPiece.keySet());
            for (var position : positionList) {
                chessBoard[position.getPosY()][position.getPosX()] = new Field(mapPosPiece.get(position), true);
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

    private Piece getNewPiece(Color color, PieceType pieceType, Position position) {
        switch (pieceType) {
            case Pawn:
                return new Pawn(color, position);
            case Rock:
                return new Rock(color, position);
            case Knight:
                return new Knight(color, position);
            case Bishop:
                return new Bishop(color, position);
            case Queen:
                return new Queen(color, position);
            case King:
                return new King(color, position);
            default:
                return null;
        }
    }

    public Map<Position, Piece> generateMapPosPiece() {
        Map<Position, Piece> mapPosPiece = new Hashtable<>();
        PieceType[] listPieceType = { PieceType.Rock, PieceType.Knight, PieceType.Bishop, PieceType.King, PieceType.Queen, PieceType.Bishop, PieceType.Knight, PieceType.Rock };
        for (int PosY = 0; PosY < 8; PosY++) {
            for (int PosX = 0; PosX < 8; PosX++) {
                if(PosY == 0) {
                    mapPosPiece.put(new Position(PosX, PosY), getNewPiece(Color.Black, listPieceType[PosX], new Position(PosX, PosY)));
                } else if(PosY == 1) {
                    mapPosPiece.put(new Position(PosX, PosY), new Pawn(Color.Black, new Position(PosX, PosY)));
                } else if(PosY == 6) {
                    mapPosPiece.put(new Position(PosX, PosY), getNewPiece(Color.White, listPieceType[PosX], new Position(PosX, PosY)));
                } else if(PosY == 7) {
                    mapPosPiece.put(new Position(PosX, PosY), new Pawn(Color.White, new Position(PosX, PosY)));
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
        return gameData.getCurrentPlayer().getPlayerUuid().trim().equals(currentPlayerUuid) ? true : false;
    }

    public void registerGameServer() {
        gameServerService.registerGameServer();
    }
}
