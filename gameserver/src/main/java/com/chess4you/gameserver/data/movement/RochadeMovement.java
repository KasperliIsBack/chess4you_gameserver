package com.chess4you.gameserver.data.movement;


import com.chess4you.gameserver.data.enums.Direction;

public class RochadeMovement {

    public Movement[] smallRochade (Position positionKing, Position positionRock) {
        if(positionKing.getPosX() == 4) {
            return new Movement[]{
                    new Movement(
                            new Position(
                                    positionKing.getPosX() + 2,
                                    positionKing.getPosY()),
                            positionKing, Direction.smallRochade),
                    new Movement(
                            new Position(
                                    positionRock.getPosX() - 2,
                                    positionRock.getPosY()),
                            positionRock, Direction.smallRochade),
            };
        } else {
            return new Movement[]{
                    new Movement(
                            new Position(
                                    positionKing.getPosX() - 2,
                                    positionKing.getPosY()),
                            positionKing, Direction.smallRochade),
                    new Movement(
                            new Position(
                                    positionRock.getPosX() + 2,
                                    positionRock.getPosY()),
                            positionRock, Direction.smallRochade),
            };
        }
    }

    public Movement[] bigRochade (Position positionKing, Position positionRock) {
        if(positionKing.getPosX() == 4) {
            return new Movement[]{
                    new Movement(
                            new Position(
                                    positionKing.getPosX() - 2,
                                    positionKing.getPosY()),
                            positionKing, Direction.bigRochade),
                    new Movement(
                            new Position(
                                    positionRock.getPosX() + 3,
                                    positionRock.getPosY()),
                            positionRock, Direction.bigRochade),
            };
        } else {
            return new Movement[]{
                    new Movement(
                            new Position(
                                    positionKing.getPosX() + 2,
                                    positionKing.getPosY()),
                            positionKing, Direction.bigRochade),
                    new Movement(
                            new Position(
                                    positionRock.getPosX() - 3,
                                    positionRock.getPosY()),
                            positionRock, Direction.bigRochade),
            };
        }
    }
}
