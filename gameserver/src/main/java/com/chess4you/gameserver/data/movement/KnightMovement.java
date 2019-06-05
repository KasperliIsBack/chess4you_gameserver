package com.chess4you.gameserver.data.movement;

import com.chess4you.gameserver.data.enums.Direction;

public class KnightMovement {

    public Movement FLKnight (Position position) {
        return new Movement(
                new Position(
                        position.getPosX() - 1,
                        position.getPosY() + 2),
                position, Direction.FLKnight);

    }

    public Movement FRKnight (Position position) {
        return new Movement(
                new Position(
                        position.getPosX() + 1,
                        position.getPosY() + 2),
                position, Direction.FRKnight);

    }

    public Movement BLKnight (Position position) {
        return new Movement(
                new Position(
                        position.getPosX() - 1,
                        position.getPosY() - 2),
                position, Direction.BLKnight);

    }

    public Movement BRKnight (Position position) {
        return new Movement(
                new Position(
                        position.getPosX() + 1,
                        position.getPosY() - 2),
                position, Direction.BRKnight);

    }
}
