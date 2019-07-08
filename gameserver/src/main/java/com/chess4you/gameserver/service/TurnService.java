package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.game.GameData;
import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.*;
import com.chess4you.gameserver.data.piece.Piece;
import com.google.gson.Gson;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TurnService {

    private TurnValidatorService turnValidatorService;
    private MovementOperation movementOperation = new MovementOperation();

    @Autowired
    public TurnService(TurnValidatorService turnValidatorService) {
        this.turnValidatorService = turnValidatorService;

    }

    public Movement[] getPossibleTurnFor(Map<Position, Piece> mapPosPiece, Piece pieceOnThatPosition, boolean reverse) {
        List<Movement> tmpMovements = new ArrayList<>();
        int distance = setDistance(pieceOnThatPosition);

        for(var direction : pieceOnThatPosition.getDirections()) {
            tmpMovements.addAll(movementsGeneral(mapPosPiece, tmpMovements, pieceOnThatPosition, direction, reverse, 0, distance ));
        }
        List<Movement> filteredMovements = new ArrayList<>();
        List<String> filteredMovementsString = new ArrayList<>();
        var gson = new Gson();
        for (var movement : tmpMovements) {
            if(!filteredMovementsString.contains(gson.toJson(movement))) {
                filteredMovements.add(movement);
                filteredMovementsString.add(gson.toJson(movement));
            }
        }
        Movement[] movements = filteredMovements.stream().toArray(Movement[]::new);
        return movements;
    }

    private int setDistance(Piece pieceOnThatPosition) {
        switch (pieceOnThatPosition.getPieceType()) {
            case King:
                return 1;
            case Pawn:
                return turnValidatorService.onStartPosition(pieceOnThatPosition) ? 2 : 1;
            default:
                return 8;
        }
    }

    public GameData doTurn(GameData gameData, Movement movement) {
        var mapPosPiece = gameData.getMapPosPiece();
        var piece = mapPosPiece.get(new Gson().toJson(movement.getOldPosition()));
        if(mapPosPiece.containsKey(new Gson().toJson(movement.getOldPosition()))) {
            mapPosPiece.remove(new Gson().toJson(movement.getNewPosition()));
        }

        mapPosPiece.put(new Gson().toJson(movement.getNewPosition()), piece);
        gameData.setMapPosPiece(mapPosPiece);
        return gameData;
    }

    private List<Movement> movementsGeneral(Map<Position, Piece> mapPosPiece, List<Movement> movementList, Piece piece, Direction direction, boolean reverse, int distance, int maxDistance){
        distance = distance == 0 ? 1 : distance;

        if(isSpecialMovement(direction)) {
            return checkSpecialMovement(movementList);
        }

        Movement movement = movementOperation.move(direction, piece.getPosition(), distance, reverse);
        var type = turnValidatorService.pieceOnPosition(mapPosPiece, movement.getNewPosition(), piece);
        switch (type) {
            case Friendly:
                return movementList;
            case Enemy:
                if(piece.getPieceType() == PieceType.Pawn) {
                    return movementList;
                } else {
                    movementList.add(movement);
                    return movementList;
                }
            case Nothing:
                if(turnValidatorService.possibleMovementOnBoard(movement)) {
                    movementList.add(movement);
                    if(distance > maxDistance + 1){
                        movementsGeneral(mapPosPiece, movementList, piece, direction, reverse, ++distance, maxDistance);
                    }
                    return movementList;
                }
                return movementList;
        }
        return movementList;
    }

    private boolean isSpecialMovement(Direction direction) {
        switch (direction) {
            case FLEnPasse:
            case FREnPasse:
            case smallRochade:
            case bigRochade:
                return true;
            default:
                return false;

        }
    }

    private List<Movement> checkSpecialMovement(List<Movement> movementList) {
        return movementList;
    }

}
