package com.chess4you.gameserver.data.movement;


import com.chess4you.gameserver.data.enums.Direction;

public class DiagonalMovement {

    public Movement FLD(Position position, int Numbers) {
        return new Movement(
                new Position(
                        position.getPosX() - Numbers,
                        position.getPosY() + Numbers),
                position, Direction.FLDiagonal);

    }

    public Movement FRD(Position position,int Numbers){
        return new Movement(
                new Position(
                        position.getPosX() + Numbers,
                        position.getPosY() + Numbers),
                position, Direction.FRDiagonal);
    }

    public Movement BLD(Position position,int Numbers){
        return new Movement(
                new Position(
                        position.getPosX() - Numbers,
                        position.getPosY() - Numbers),
                position, Direction.BLDiagonal);
    }

    public Movement BRD(Position position,int Numbers){
        return new Movement(
                new Position(
                        position.getPosX() + Numbers,
                        position.getPosY() - Numbers),
                position, Direction.BRDiagonal);
    }
}
