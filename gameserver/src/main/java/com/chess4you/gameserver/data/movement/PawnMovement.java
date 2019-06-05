package com.chess4you.gameserver.data.movement;

import com.chess4you.gameserver.data.enums.Direction;

public class PawnMovement {
    public Movement FLEnPasse(Position position) {
        return new Movement(
                new Position(
                        position.getPosX() - 1,
                        position.getPosY() + 1),
                position, Direction.FLEnPasse);
    }

    public Movement FREnPasse(Position position) {
        return new Movement(
                new Position(
                        position.getPosX() + 1,
                        position.getPosY() + 1),
                position, Direction.FREnPasse);
    }
}
