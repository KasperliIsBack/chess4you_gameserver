package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.enums.PositionType;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnValidatorService {

    public boolean onStartPosition(Piece piece) {
        switch (piece.getType()) {
            case Pawn:
                return piece.getPosition().getPosY() == 1
                        || piece.getPosition().getPosY() == 6;
            case King:
                return (piece.getPosition().getPosY() == 0
                        && piece.getPosition().getPosX() == 3)
                        || (piece.getPosition().getPosY() == 7
                        && piece.getPosition().getPosX() == 3);
            case Rock:
                return (piece.getPosition().getPosY() == 0
                        && piece.getPosition().getPosX() == 0)
                        || (piece.getPosition().getPosY() == 0
                        && piece.getPosition().getPosX() == 7)
                        || (piece.getPosition().getPosY() == 7
                        && piece.getPosition().getPosX() == 0)
                        || (piece.getPosition().getPosY() == 7
                        && piece.getPosition().getPosX() == 7);
            default:
                return false;
        }
    }

    public PositionType pieceOnPosition(Dictionary<Position, Piece> listPosPieceType, Position position, Piece piece) {
       ArrayList<Position> listPosition = Collections.list(listPosPieceType.keys());
       for(var position2 : listPosition) {
            if( position2.getPosX() == position.getPosX() &&
                    position2.getPosY() == position.getPosY()) {
                if(listPosPieceType.get(position2).getColor() == piece.getColor()) {
                    return PositionType.Friendly;
                } else {
                    return PositionType.Enemeny;
                }
            }
        }
        return PositionType.Nothing;
    }

    public boolean possibleMovementOnBoard(Movement movement) {
       return movement.getNewPosition().getPosY() >= 0
               && movement.getNewPosition().getPosY() <= 7
               && movement.getNewPosition().getPosX() >= 0
               && movement.getNewPosition().getPosX() <= 7;
    }

    public boolean isRochadePossible(Dictionary<Position, Piece> DicPosPiece, Piece piece) {
        List<Position> listPosition =  Collections.list(DicPosPiece.keys()).stream()
                .filter(position ->
                    position.getPosY() == piece.getPosition().getPosY()
                )
                .collect(Collectors.toList());
        listPosition.remove(piece.getPosition());

        if(listPosition.size() == 1){
            if(onStartPosition(piece)) {
                if(onStartPosition(DicPosPiece.get(piece.getPosition()))) {
                    return true;
                }
            }
        }
        return false;
   }

    public Direction rochadeType(Dictionary<Position, Piece> DicPosPiece, Piece piece) {
        List<Position> listPosition =  Collections.list(DicPosPiece.keys()).stream()
                .filter(position ->
                        position.getPosY() == piece.getPosition().getPosY()
                )
                .collect(Collectors.toList());
        listPosition.remove(piece.getPosition());
        return (listPosition.get(0).getPosX() == 0 && listPosition.get(0).getPosY() == 0)
                || (listPosition.get(0).getPosX() == 7 && listPosition.get(0).getPosY() == 7)
                ? Direction.bigRochade : Direction.smallRochade;
    }

    public boolean isEnPassePossible(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
        List<Position> tmpPositions = Collections.list(dicPosPiece.keys());
        int posXPiece = piece.getPosition().getPosX();
        int posYPiece = piece.getPosition().getPosY();
        Position posForward;
        Position posLeft;
        Position posForwardLeft;
        Position posRight;
        Position posForwardRight;
        if(piece.getColor() == Color.Black) {
            posForward = new Position(posXPiece, posYPiece + 1);
            posLeft = new Position(posXPiece - 1, posYPiece);
            posForwardLeft = new Position(posXPiece - 1, posYPiece - 1);
            posRight = new Position(posXPiece + 1, posYPiece);
            posForwardRight = new Position(posXPiece + 1, posYPiece - 1);
        } else {
            posForward = new Position(posXPiece, posYPiece - 1);
            posLeft = new Position(posXPiece - 1, posYPiece);
            posForwardLeft = new Position(posXPiece - 1, posYPiece + 1);
            posRight = new Position(posXPiece + 1, posYPiece);
            posForwardRight = new Position(posXPiece + 1, posYPiece + 1);
        }
        if(onStartPosition(piece)) {
            if(containsPosition(tmpPositions, posForward)) {
                if(positionValid(posLeft)) {
                    if(containsPosition(tmpPositions, posLeft) && !containsPosition(tmpPositions, posForwardLeft)) {
                        return true;
                    }
                }
                if(positionValid(posRight)) {
                    if(containsPosition(tmpPositions, posRight) && !containsPosition(tmpPositions, posForwardRight)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean containsPosition(List<Position> listPosition, Position testPosition){
        for (var position : listPosition) {
            if(position.getPosY() == testPosition.getPosY()
            && position.getPosX() == testPosition.getPosX()) {
                return true;
            }
        }
        return false;
    }

    public boolean positionValid(Position position) {
        if(position.getPosX() >= 0 && position.getPosX() <= 7
        && position.getPosY() >= 0 && position.getPosY() <= 7) {
            return true;
        } else {
            return false;
        }
    }
}
