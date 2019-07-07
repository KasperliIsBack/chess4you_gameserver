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
import java.util.stream.Collectors;

@Service
public class TurnService {

    private TurnValidatorService turnValidatorService;
    private MovementOperation movementOperation = new MovementOperation();

    @Autowired
    public TurnService(TurnValidatorService turnValidatorService) {
        this.turnValidatorService = turnValidatorService;

    }

    public Movement[] getPossibleTurnFor(Map<Position, Piece> mapPosPiece, Piece pieceOnThatPosition, boolean reverse) {
        var tmpMovements = new ArrayList<Movement>();
        for(var directionsType : pieceOnThatPosition.getDirectionTypes()) {
            switch (directionsType) {
                case Linear:
                    tmpMovements.addAll(linearMovements(mapPosPiece, pieceOnThatPosition, reverse));
                    break;
                case Diagonal:
                    tmpMovements.addAll(diagonalMovements(mapPosPiece, pieceOnThatPosition, reverse));
                    break;
                case Pawn:
                    tmpMovements.addAll(enPasseMovements(mapPosPiece, pieceOnThatPosition, reverse));
                    break;
                case Knight:
                    tmpMovements.addAll(knightMovements(mapPosPiece, pieceOnThatPosition, reverse));
                    break;
                case Rochade:
                    tmpMovements.addAll(rochadeMovements(mapPosPiece, pieceOnThatPosition, reverse));
            }
        }
        Movement[] movements = tmpMovements.stream().toArray(Movement[]::new);
        return movements;
    }

    public GameData doTurn(GameData gameData, Movement movement) {
        var mapPosPiece = gameData.getMapPosPiece();
        var piece = mapPosPiece.get(movement.getOldPosition());
        if(mapPosPiece.containsKey(movement.getNewPosition())) {
            mapPosPiece.remove(movement.getNewPosition());
        }

        mapPosPiece.put(new Gson().toJson(movement.getNewPosition()), piece);
        gameData.setMapPosPiece(mapPosPiece);
        return gameData;
    }

    private List<Movement> rochadeMovements(Map<Position, Piece> mapPosPiece, Piece piece, boolean reverse) {
        var tmp = new ArrayList<Movement>();
        if(turnValidatorService.isRochadePossible(mapPosPiece, piece)) {
            List<Position> listPiece =  new ArrayList<>(mapPosPiece.keySet());
            listPiece.addAll(mapPosPiece.keySet());
            listPiece.stream()
                    .filter(position ->
                            position.getPosY() == piece.getPosition().getPosY()
                                    && position.getPosX() != piece.getPosition().getPosX()
                    )
                    .collect(Collectors.toList());
            var rock = mapPosPiece.get(listPiece.get(0));
            switch (turnValidatorService.rochadeType(mapPosPiece, piece)) {
                case smallRochade:
                    tmp.addAll(Arrays.asList(movementOperation.smallRochade(piece.getPosition(), rock.getPosition())));
                    break;
                case bigRochade:
                    tmp.addAll(Arrays.asList(movementOperation.bigRochade(piece.getPosition(), rock.getPosition())));
                    break;
            }
        }
        return tmp;
    }


    public List<Movement> diagonalMovements(Map<Position, Piece> mapPosPiece, Piece piece, boolean reverse) {
        int number;
        switch (piece.getPieceType()){
            case King:
                number = 1;
                break;
            default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(Direction direction : piece.getDirections()) {
            tmp.addAll(movementsGeneral(direction, 0, number, reverse, mapPosPiece, piece, new ArrayList<>()));
        }
        return tmp;
    }

    public List<Movement> linearMovements(Map<Position, Piece> mapPosPiece, Piece piece, boolean reverse) {
        int number;
        switch (piece.getPieceType()){
            case King:
                number = 1;
                break;
            case Pawn:
                number = turnValidatorService.onStartPosition(piece) ? 2 : 1;
                break;
                default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(Direction direction : piece.getDirections()) {
            tmp.addAll(movementsGeneral(direction, 0, number, reverse, mapPosPiece, piece, new ArrayList<>()));
        }
        return tmp;
    }

    private List<Movement> enPasseMovements(Map<Position, Piece> mapPosPiece, Piece piece, boolean reverse){
        var tmp = new ArrayList<Movement>();
        for(Direction direction : piece.getDirections()) {
            switch (direction) {
                case FLEnPasse:
                case FREnPasse:
                    if(turnValidatorService.isEnPassePossible(mapPosPiece, piece)) {
                        Movement movement = movementOperation.move(direction, piece.getPosition(), 1, reverse);
                        var type = turnValidatorService.pieceOnPosition(mapPosPiece, movement.getNewPosition(), piece);
                        switch (type) {
                            case Friendly:
                                break;
                            case Enemy:
                            case Nothing:
                                tmp.add(movement);
                        }
                        break;
                    }
            }
        }
        return tmp;
    }

    public List<Movement> knightMovements(Map<Position, Piece> mapPosPiece, Piece piece, boolean reverse) {
        int number = 1;
        var tmp = new ArrayList<Movement>();
        for(Direction direction : piece.getDirections()) {
            tmp.addAll(movementsGeneral(direction, 0, number, reverse, mapPosPiece, piece, new ArrayList<>()));
        }
        return tmp;
    }

    private List<Movement> movementsGeneral(Direction direction, int counter, int number, boolean reverse, Map<Position, Piece> mapPosPiece, Piece piece, List<Movement> listMovementData){
        counter = counter == 0 ? 1 : counter;
        Movement movement = movementOperation.move(direction, piece.getPosition(), counter, reverse);
        var type = turnValidatorService.pieceOnPosition(mapPosPiece, movement.getNewPosition(), piece);
        switch (type) {
            case Friendly:
                return listMovementData;
            case Enemy:
                if(piece.getPieceType() == PieceType.Pawn) {
                    return listMovementData;
                } else {
                    listMovementData.add(movement);
                    return listMovementData;
                }
            case Nothing:
                if(turnValidatorService.possibleMovementOnBoard(movement)) {
                    listMovementData.add(movement);
                    if(counter < number){
                        movementsGeneral(direction, ++counter, number, reverse, mapPosPiece, piece, listMovementData);
                    }
                    return listMovementData;
                }
                return listMovementData;
        }
        return listMovementData;
    }
}
