package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.*;
import com.chess4you.gameserver.data.piece.Piece;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TurnService {

    private TurnValidatorService turnValidatorService;


    private LinearMovement linearMovements = new LinearMovement();
    private DiagonalMovement diagonalMovements = new DiagonalMovement();
    private KnightMovement knightMovements = new KnightMovement();
    private RochadeMovement rochadeMovement = new RochadeMovement();

    @Autowired
    public TurnService(TurnValidatorService turnValidatorService) {
        this.turnValidatorService = turnValidatorService;

    }

    public Movement[] getPossibleTurnFor(Map<Position, Piece> mapPosPiece, Piece pieceOnThatPosition) {
        var tmpMovements = new ArrayList<Movement>();
        for (int i = 0; i < pieceOnThatPosition.getDirections().length; i++) {
            switch (pieceOnThatPosition.getDirections()[i]) {
                case Linear:
                    try {
                        tmpMovements.addAll(linearMovements(mapPosPiece, pieceOnThatPosition));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    break;
                case Diagonal:
                    tmpMovements.addAll(diagonalMovements(mapPosPiece, pieceOnThatPosition));
                    break;
                case Pawn:
                    tmpMovements.addAll(enPasseMovements(mapPosPiece, pieceOnThatPosition));
                    break;
                case Knight:
                    tmpMovements.addAll(knightMovements(mapPosPiece, pieceOnThatPosition));
                    break;
                case Rochade:
                    tmpMovements.addAll(rochadeMovements(mapPosPiece, pieceOnThatPosition));
            }
        }
        return tmpMovements.stream().toArray(Movement[]::new);
    }

    public GameData doTurn(GameData gameData, Movement movement) {
        var mapPosPiece = gameData.getMapPosPiece();
        var piece = mapPosPiece.get(movement.getOldPosition());
        if(mapPosPiece.containsKey(movement.getNewPosition())) {
            mapPosPiece.remove(movement.getNewPosition());
        }

        mapPosPiece.put(movement.getNewPosition(), piece);
        gameData.setMapPosPiece(mapPosPiece);
        return gameData;
    }

    private List<Movement> rochadeMovements(Map<Position, Piece> mapPosPiece, Piece piece) {
        var tmp = new ArrayList<Movement>();
        if(turnValidatorService.isRochadePossible(mapPosPiece, piece)) {
            List<Position> listPiece =  new ArrayList<>();
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
                    tmp.addAll(Arrays.asList(rochadeMovement.smallRochade(piece.getPosition(), rock.getPosition())));
                    break;
                case bigRochade:
                    tmp.addAll(Arrays.asList(rochadeMovement.bigRochade(piece.getPosition(), rock.getPosition())));
                    break;
            }
        }
        return tmp;
    }

    public List<Movement> diagonalMovements(Map<Position, Piece> mapPosPiece, Piece piece) {
        int number;
        switch (piece.getType()){
            case King:
                number = 1;
                break;
            default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(var method : diagonalMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, mapPosPiece, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> linearMovements(Map<Position, Piece> mapPosPiece, Piece piece) throws NoSuchMethodException {
        int number;
        switch (piece.getType()){
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
        if(piece.getType() == PieceType.Pawn) {
            tmp.addAll(movementsGeneral(linearMovements, linearMovements.getClass().getMethod("F", Position.class, int.class), 0, number, mapPosPiece, piece, new ArrayList<>()));
        } else {
            for(var method : linearMovements.getClass().getMethods()){
                if(method.getReturnType() == Movement.class) {
                    tmp.addAll(movementsGeneral(linearMovements, method, 0, number, mapPosPiece, piece, new ArrayList<>()));
                } else {
                    break;
                }
            }
        }
        return tmp;
    }

    public List<Movement> enPasseMovements(Map<Position, Piece> mapPosPiece, Piece piece){
        List<Position> tmpPositions =  new ArrayList<>();
        tmpPositions.addAll(mapPosPiece.keySet());
        List<Movement> tmpMovements = new ArrayList<Movement>();
        Position posForwardLeft;
        Position posForwardRight;
        if(piece.getColor() == Color.Black) {
            posForwardLeft = new Position(piece.getPosition().getPosX() - 1, piece.getPosition().getPosY() + 1);
            posForwardRight = new Position(piece.getPosition().getPosX() + 1, piece.getPosition().getPosY());

        } else {
            posForwardLeft = new Position(piece.getPosition().getPosX() - 1, piece.getPosition().getPosY() - 1);
            posForwardRight = new Position(piece.getPosition().getPosX() + 1, piece.getPosition().getPosY());
        }
        if(turnValidatorService.isEnPassePossible(mapPosPiece, piece)) {
            if(turnValidatorService.containsPosition(tmpPositions, posForwardLeft)) {
                var type = turnValidatorService.pieceOnPosition(mapPosPiece, posForwardLeft, piece);
                switch (type) {
                    case Friendly:
                        break;
                    case Enemeny:
                    case Nothing:
                        tmpMovements.add(new Movement(posForwardLeft, piece.getPosition(), Direction.FLEnPasse));
                }
            } else if(turnValidatorService.containsPosition(tmpPositions, posForwardRight)) {
                var type = turnValidatorService.pieceOnPosition(mapPosPiece, posForwardRight, piece);
                switch (type) {
                    case Friendly:
                        break;
                    case Enemeny:
                    case Nothing:
                        tmpMovements.add(new Movement(posForwardRight, piece.getPosition(), Direction.FREnPasse));
                }
            }
        }
        return tmpMovements;
    }

    public List<Movement> knightMovements(Map<Position, Piece> mapPosPiece, Piece piece) {
        int number = 1;
        var tmp = new ArrayList<Movement>();
        for(var method : knightMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, mapPosPiece, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> movementsGeneral(Object instance, Method method, int counter, int number, Map<Position, Piece> mapPosPiece, Piece piece, List<Movement> listMovements){
        counter = counter == 0 ? 1 : counter;
        try{
            Object tmp = number == 0 ? method.invoke(instance, piece.getPosition()) :
                    method.invoke(instance, piece.getPosition(), counter);
            Movement movement = (Movement) tmp;
           var type = turnValidatorService.pieceOnPosition(mapPosPiece, movement.getNewPosition(), piece);
            switch (type) {
                case Friendly:
                    return listMovements;
                case Enemeny:
                    if(piece.getType() == PieceType.Pawn) {
                        return listMovements;
                    } else {
                        listMovements.add(movement);
                        return listMovements;
                    }
                case Nothing:
                    if(turnValidatorService.possibleMovementOnBoard(movement)) {
                        listMovements.add(movement);
                        if(counter < number){
                            movementsGeneral(instance, method, ++counter, number, mapPosPiece, piece, listMovements);
                        }
                        return listMovements;
                    }
                    return  listMovements;
            }
            return listMovements;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}
