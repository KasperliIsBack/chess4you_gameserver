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

    public Movement[] getPossibleTurnFor(Dictionary<Position, Piece> dicPosPiece, Piece pieceOnThatPosition) {
        var tmpMovements = new ArrayList<Movement>();
        for (int i = 0; i < pieceOnThatPosition.getDirections().length; i++) {
            switch (pieceOnThatPosition.getDirections()[i]) {
                case Linear:
                    tmpMovements.addAll(linearMovements(dicPosPiece, pieceOnThatPosition));
                    break;
                case Diagonal:
                    tmpMovements.addAll(diagonalMovements(dicPosPiece, pieceOnThatPosition));
                    break;
                case Pawn:
                    tmpMovements.addAll(enPasseMovements(dicPosPiece, pieceOnThatPosition));
                    break;
                case Knight:
                    tmpMovements.addAll(knightMovements(dicPosPiece, pieceOnThatPosition));
                    break;
                case Rochade:
                    tmpMovements.addAll(rochadeMovements(dicPosPiece, pieceOnThatPosition));
            }
        }
        return (Movement[]) tmpMovements.toArray();
    }

    public GameData doTurn(GameData gameData, Movement movement) {
        var dicPosPiece = gameData.getDicPosPiece();
        var piece = dicPosPiece.get(movement.getOldPosition());
        if(Collections.list(dicPosPiece.keys()).contains(movement.getNewPosition())) {
            dicPosPiece.remove(movement.getNewPosition());
        }

        dicPosPiece.put(movement.getNewPosition(), piece);
        gameData.setDicPosPiece(dicPosPiece);
        return gameData;
    }

    private List<Movement> rochadeMovements(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
        if(piece.getType() == PieceType.Knight) {
            var tmp = new ArrayList<Movement>();
            if(turnValidatorService.rochadePossible(dicPosPiece, piece)) {
                var indexRock =  Collections.list(dicPosPiece.keys())
                        .stream()
                        .filter(position ->
                                position.getPosY() == piece.getPosition().getPosY()
                                        && position.getPosX() != piece.getPosition().getPosX()
                        )
                        .collect(Collectors.toList());
                var rock = dicPosPiece.get(indexRock.get(0));
                switch (turnValidatorService.rochadeType(dicPosPiece, piece)) {
                    case smallRochade:
                        tmp.addAll(Arrays.asList(rochadeMovement.smallRochade(piece.getPosition(), rock.getPosition())));
                        break;
                    case bigRochade:
                        tmp.addAll(Arrays.asList(rochadeMovement.bigRochade(piece.getPosition(), rock.getPosition())));
                        break;
                }
            }
            return tmp;
        } else {
            return null;
        }
    }

    public List<Movement> diagonalMovements(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
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
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, dicPosPiece, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> linearMovements(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
        int number;
        switch (piece.getType()){
            case King:
                number = 1;
                break;
            case Pawn:
                number = turnValidatorService.onStartPosition(piece) ? 2 : 1;
            default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(var method : linearMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, dicPosPiece, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> enPasseMovements(Dictionary<Position, Piece> dicPosPiece, Piece piece){
        List<Position> tmpPositions = Collections.list(dicPosPiece.keys());
        List<Movement> tmpMovements = new ArrayList<Movement>();
        Position posForwardLeft;
        Position posForwardRight;
        if(piece.getColor() == Color.Black) {
            posForwardLeft = new Position(piece.getPosition().getPosX() - 1, piece.getPosition().getPosY() + 1);
            posForwardRight = new Position(piece.getPosition().getPosX() + 1, piece.getPosition().getPosY() + 1);

        } else {
            posForwardLeft = new Position(piece.getPosition().getPosX() - 1, piece.getPosition().getPosY() - 1);
            posForwardRight = new Position(piece.getPosition().getPosX() + 1, piece.getPosition().getPosY() - 1);
        }
        if(tmpPositions.contains(posForwardLeft)) {
            var type = turnValidatorService.pieceOnPosition(dicPosPiece, posForwardLeft, piece);
            switch (type) {
                case Friendly:
                    break;
                case Enemeny:
                case Nothing:
                    tmpMovements.add(new Movement(posForwardLeft, piece.getPosition(), Direction.FLEnPasse));
            }
        } else if(tmpPositions.contains(posForwardRight)) {
            var type = turnValidatorService.pieceOnPosition(dicPosPiece, posForwardRight, piece);
            switch (type) {
                case Friendly:
                    break;
                case Enemeny:
                case Nothing:
                    tmpMovements.add(new Movement(posForwardRight, piece.getPosition(), Direction.FREnPasse));
            }
        }
        return tmpMovements;
    }

    public List<Movement> knightMovements(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
        int number = 1;
        var tmp = new ArrayList<Movement>();
        for(var method : knightMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, dicPosPiece, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> movementsGeneral(Object instance, Method method, int counter, int number, Dictionary<Position, Piece> dicPosPiece, Piece piece, List<Movement> listMovements){
        counter = counter == 0 ? 1 : counter;
        try{
            Object tmp = number == 0 ? method.invoke(instance, piece.getPosition()) :
                    method.invoke(instance, piece.getPosition(), counter);
            Movement movement = (Movement) tmp;
           var type = turnValidatorService.pieceOnPosition(dicPosPiece, movement.getNewPosition(), piece);
            switch (type) {
                case Friendly:
                    return listMovements;
                case Enemeny:
                    listMovements.add(movement);
                    return listMovements;
                case Nothing:
                    if(turnValidatorService.possibleMovementOnBoard(movement)) {
                        listMovements.add(movement);
                        if(counter < number){
                            movementsGeneral(instance, method, ++counter, number, dicPosPiece, piece, listMovements);
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
