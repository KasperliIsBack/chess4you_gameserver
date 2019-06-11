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

    public boolean rochadePossible(Dictionary<Position, Piece> DicPosPiece, Piece piece) {
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

    public boolean enPassePossible(Dictionary<Position, Piece> dicPosPiece, Piece piece) {
        List<Position> tmpPositions = Collections.list(dicPosPiece.keys());
        var posXPiece = piece.getPosition().getPosX();
        var posYPiece = piece.getPosition().getPosY();
        if(piece.getColor() == Color.Black) {
            Position posForwardBlack = new Position(posXPiece, posYPiece + 1);
            Position posForwardLeftBlack = new Position(posXPiece - 1, posYPiece + 1);
            Position posForwardRightBlack = new Position(posXPiece + 1, posYPiece + 1);
            if(onStartPosition(piece)) {
                if(tmpPositions.contains(posForwardBlack)) {
                    if(positionValid(posForwardLeftBlack)) {
                        if(tmpPositions.contains(posForwardLeftBlack)) {
                            return true;
                        }
                    } else if(positionValid(posForwardRightBlack)) {
                        if(tmpPositions.contains(posForwardRightBlack)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            Position posForwardWhite = new Position(posXPiece, posYPiece - 1);
            Position posForwardLeftWhite = new Position(posXPiece - 1, posYPiece - 1);
            Position posForwardRightWhite = new Position(posXPiece + 1, posYPiece - 1);
            if(onStartPosition(piece)) {
                if(tmpPositions.contains(posForwardWhite)) {
                  if(positionValid(posForwardLeftWhite)) {
                      if(tmpPositions.contains(posForwardLeftWhite)) {
                          return true;
                      }
                  } else if(positionValid(posForwardRightWhite)) {
                      if(tmpPositions.contains(posForwardRightWhite)) {
                          return true;
                      }
                  }
                }
            }
            return false;
        }
    }

    public boolean positionValid(Position position) {
        if(position.getPosX() >= 1 && position.getPosX() <= 7
        && position.getPosY() >= 1 && position.getPosY() <= 7) {
            return true;
        } else {
            return false;
        }
    }
}
