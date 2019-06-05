package com.chess4you.gameserver.data.movement;

import com.chess4you.gameserver.data.enums.Direction;

public class LinearMovement {
    public Movement F(Position position, int Numbers) {
        return new Movement(
                new Position(
                        position.getPosX(),
                        position.getPosY()  + Numbers),
                position, Direction.Forward);

    }
    public Movement B(Position position,int Numbers) {
        return new Movement(
                new Position(
                        position.getPosX(),
                        position.getPosY() - Numbers),
                position, Direction.Backward);

    }
    public Movement L(Position position,int Numbers) {
        return new Movement(
                new Position(
                        position.getPosX() - Numbers,
                        position.getPosY()),
                position, Direction.Left);

    }
    public Movement R(Position position,int Numbers) {
        return new Movement(
                new Position(
                        position.getPosX() + Numbers,
                        position.getPosY()),
                position, Direction.Right);

    }
}
