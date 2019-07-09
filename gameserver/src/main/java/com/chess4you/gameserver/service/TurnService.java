package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.enums.PositionType;
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

    public Movement[] getPossibleTurnFor(Map<Position, Piece> mapPosPiece, Piece piece, boolean isReverse) {
        List<Movement> tmpMovements = new ArrayList<>();
        int maxDistance = setDistance(piece);

        for(var direction : piece.getDirections()) {
            addNewPosition(tmpMovements, generalMovement(mapPosPiece, tmpMovements, piece, direction,1, maxDistance, isReverse));
        }
        /*List<Movement> filteredMovements = new ArrayList<>();
        List<String> filteredMovementsString = new ArrayList<>();
        var gson = new Gson();
        for (var movement : tmpMovements) {
            if(!filteredMovementsString.contains(gson.toJson(movement))) {
                filteredMovements.add(movement);
                filteredMovementsString.add(gson.toJson(movement));
            }
        }*/
        Movement[] movements = tmpMovements.stream().toArray(Movement[]::new);
        return movements;
    }

    private void addNewPosition(List<Movement> tmpMovements, List<Movement> generalMovement) {
        if(tmpMovements.size() != 0) {
            if(generalMovement.size() != 0) {
                for(Movement movement : generalMovement) {
                    if(!tmpMovements.contains(movement)) {
                        tmpMovements.add(movement);
                    }
                }
            } else {
                return;
            }
        } else {
            tmpMovements.addAll(generalMovement);
        }
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

        Map<String, Piece> mapPosPiece = gameData.getMapPosPiece();
        String newPosition = new Gson().toJson(movement.getNewPosition());
        String oldPosition = new Gson().toJson(movement.getOldPosition());
        Piece piece = mapPosPiece.get(oldPosition);
        piece.setPosition(movement.getNewPosition());
        mapPosPiece.remove(oldPosition);

        if(mapPosPiece.containsKey(newPosition)) {
            mapPosPiece.remove(newPosition);
            mapPosPiece.put(newPosition, piece);
        } else {
            mapPosPiece.put(newPosition, piece);
        }
        gameData.setMapPosPiece(mapPosPiece);
        return gameData;
    }

    private List<Movement> generalMovement(Map<Position, Piece> mapPosPiece, List<Movement> movements, Piece piece,
                                           Direction direction, int distance, int maxDistance, boolean isReverse) {
        Movement tmpMovement = null;
        PositionType tmpPositionType = null;

        if(isSpecialDirection(direction)) {
            if(checkIfSpecialMovementIsPossible()) {
                //tmpMovement = null;
                //tmpPositionType =  turnValidatorService.pieceOnPosition(mapPosPiece, tmpMovement.getNewPosition(), piece);
                return movements;
            } else {
                return movements;
            }
        } else {
            tmpMovement = movementOperation.move(direction, piece.getPosition(), distance, isReverse);
            tmpPositionType =  turnValidatorService.pieceOnPosition(mapPosPiece, tmpMovement.getNewPosition(), piece);
        }
        System.out.println("Method passed " + direction + " "+ tmpPositionType + " " + new Gson().toJson(tmpMovement));
        switch (tmpPositionType) {
            case Friendly:
                break;
            case Enemy:
                if(piece.getPieceType() != PieceType.Pawn) {
                    movements.add(tmpMovement);
                }
                return movements;
            case Nothing:
                if(turnValidatorService.possibleMovementOnBoard(tmpMovement)) {
                    movements.add(tmpMovement);
                    if(distance < maxDistance) {
                        generalMovement(mapPosPiece, movements, piece, direction, ++distance, maxDistance, isReverse);
                    } else{
                        return movements;
                    }
                } else {
                    return movements;
                }
            default:
                return  movements;
        }
        return movements;
    }

    private List<Movement> movementsGeneral1(Map<Position, Piece> mapPosPiece, List<Movement> movementList, Piece piece, Direction direction, boolean reverse, int distance, int maxDistance){
        distance = distance == 0 ? 1 : distance;

        if(isSpecialDirection(direction)) {
            return  null;
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
                    if(distance < maxDistance){
                        //generalMovement(mapPosPiece, movementList, piece, direction, reverse, ++distance, maxDistance);
                    }
                }
        }
        return movementList;
    }

    private boolean isSpecialDirection(Direction direction) {
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

    private boolean checkIfSpecialMovementIsPossible() {
        return true;
    }

}
