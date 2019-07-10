package com.chess4you.gameserver.data.movement;

import com.chess4you.gameserver.data.enums.Direction;
import lombok.var;

public class MovementOperation {

    private Position getNewPosition(Direction direction, Position oldPosition, int distance, boolean reverse) {
        var PosX = oldPosition.getPosX();
        var PosY = oldPosition.getPosY();
        int setPosYPositive = reverse ? PosY - distance : PosY + distance;
        int setPosYNegative = reverse ? PosY + distance : PosY - distance;

        int setPosXPositive = reverse ? PosX - distance : PosX + distance;
        int setPosXNegative = reverse ? PosX + distance : PosX - distance;

        int setPosXPositiveOneDistance = reverse ? PosX + 1 : PosX - 1;
        int setPosXNegativeOneDistance = reverse ? PosX - 1 : PosX + 1;

        int setPosYPositiveOneDistance = reverse ? PosY - 1 : PosY + 1;
        int setPosYPositiveTwoDistance = reverse ? PosY - 2 : PosY + 2;

        int setPosYNegativeTwoDistance = reverse ? PosY + 2 : PosY - 2;

        switch (direction) {
            case Forward:
                PosY = setPosYPositive;
                break;
            case Backward:
                PosY = setPosYNegative;
                break;
            case Left:
                PosX = setPosXNegative;
                break;
            case Right:
                PosX = setPosXPositive;
                break;
            case FLDiagonal:
                PosX = setPosXNegative;
                PosY = setPosYPositive;
                break;
            case FRDiagonal:
                PosX = setPosXPositive;
                PosY = setPosYPositive;
                break;
            case BLDiagonal:
                PosX = setPosXNegative;
                PosY = setPosYNegative;
                break;
            case BRDiagonal:
                PosX = setPosXPositive;
                PosY = setPosYNegative;
                break;
            case FLKnight:
                PosX = setPosXNegativeOneDistance;
                PosY = setPosYPositiveTwoDistance;
                break;
            case FRKnight:
                PosX = setPosXPositiveOneDistance;
                PosY = setPosYPositiveTwoDistance;
                break;
            case BLKnight:
                PosX = setPosXNegativeOneDistance;
                PosY = setPosYNegativeTwoDistance;
                break;
            case BRKnight:
                PosX = setPosXPositiveOneDistance;
                PosY = setPosYNegativeTwoDistance;
                break;
            case FLEnPasse:
                PosX = setPosXNegativeOneDistance;
                PosY = setPosYPositiveOneDistance;
                break;
            case FREnPasse:
                PosX = setPosXPositiveOneDistance;
                PosY = setPosYPositiveOneDistance;
                break;
        }
        return new Position(PosX, PosY);
    }

    public Movement move(Direction direction, Position oldPosition, int distance, boolean reverse) {
        var newPosition = getNewPosition(direction, oldPosition, distance, reverse);
        return new Movement(newPosition, oldPosition, Direction.Forward);
    }

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
