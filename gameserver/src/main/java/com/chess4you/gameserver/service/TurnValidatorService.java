package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.EnPasseResult;
import lombok.var;
import org.chess4you.server.data.chessboard.board.ChessEnum;
import org.chess4you.server.data.chessboard.movements.base.Movement;
import org.chess4you.server.data.chessboard.movements.base.Position;
import org.chess4you.server.data.chessboard.pieces.PieceType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TurnValidatorService {

    public boolean onStartPosition(PieceType piece) {
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

    public PositionType pieceOnPosition(Dictionary<Position, PieceType> listPosPieceType, Position position, PieceType piece) {
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

    public boolean movementAreEqual(Movement firstMovement, Movement secondMovement) {
       if((firstMovement.getNewPosition().getPosY() == secondMovement.getNewPosition().getPosY())
               && (firstMovement.getNewPosition().getPosX() == secondMovement.getNewPosition().getPosX())
               && (firstMovement.getOldPosition().getPosY() == secondMovement.getOldPosition().getPosY())
               && (firstMovement.getOldPosition().getPosX() == secondMovement.getOldPosition().getPosX())
               && (firstMovement.getDirection() == secondMovement.getDirection())) {
           return true;
       } else  {
           return false;
       }
    }

    public boolean rochadePossible(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        List<Position> listPosition =  Collections.list(listPieceType.keys()).stream()
                .filter(position ->
                    position.getPosY() == piece.getPosition().getPosY()
                )
                .collect(Collectors.toList());
        listPosition.remove(piece.getPosition());

        if(listPosition.size() == 1){
            if(onStartPosition(piece)) {
                if(onStartPosition(listPieceType.get(piece.getPosition()))) {
                    return true;
                }
            }
        }
        return false;
   }

    public Direction rochadeType(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        List<Position> listPosition =  Collections.list(listPieceType.keys()).stream()
                .filter(position ->
                        position.getPosY() == piece.getPosition().getPosY()
                )
                .collect(Collectors.toList());
        listPosition.remove(piece.getPosition());
        return (listPosition.get(0).getPosX() == 0 && listPosition.get(0).getPosY() == 0)
                || (listPosition.get(0).getPosX() == 7 && listPosition.get(0).getPosY() == 7)
                ? Direction.bigRochade : Direction.smallRochade;
    }

    public EnPasseResult enPassePossible(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        List<Position> listPosition = Collections.list(listPieceType.keys());
        listPosition.remove(piece.getPosition());

        Position blackPosForward = piece.getPosition();
        blackPosForward.setPosY(piece.getPosition().getPosY() - 1);

        Position whitePosForward = piece.getPosition();
        whitePosForward.setPosY(piece.getPosition().getPosY() + 1);

        if(listPosition.contains(blackPosForward)) {
            return getPossibleEnPasse(listPosition,piece, Color.Black);
        } else if(listPosition.contains(whitePosForward)) {
            return getPossibleEnPasse(listPosition,piece, Color.White);
        } else {
            return new EnPasseResult(false);
        }
    }

    public EnPasseResult getPossibleEnPasse(List<Position> listPosition, PieceType piece, Color color){
        int number = color == Color.Black ? -1: +1;
        Position posLeft = piece.getPosition();
        Position posRight = piece.getPosition();
        posLeft.setPosX(piece.getPosition().getPosX() - 1);
        posRight.setPosX(piece.getPosition().getPosX() + 1);
        Movement movementOne = null;
        Movement movementTwo = null;

        if(listPosition.contains(posLeft)) {
            posLeft.setPosY(posLeft.getPosY() + number);
            if(listPosition.contains(posLeft)) {
                movementOne = new Movement(posLeft, piece.getPosition(), Direction.FLEnPasse);
            }
        } else if(listPosition.contains(posRight)) {
            posRight.setPosY(posRight.getPosY() + number);
            if(listPosition.contains(posRight)) {
                movementTwo = new Movement(posLeft, piece.getPosition(), Direction.FREnPasse);
            }
        } else {
            return new EnPasseResult(false);
        }
        if(movementOne != null && movementTwo != null) {
            return new EnPasseResult(true, Arrays.asList(movementOne, movementTwo));
        } else if(movementOne != null && movementTwo == null) {
            return new EnPasseResult(true, Arrays.asList(movementOne));
        } else if(movementOne == null && movementTwo != null) {
            return new EnPasseResult(true, Arrays.asList(movementTwo));
        } else{
            return new EnPasseResult(false);
        }
    }
}
