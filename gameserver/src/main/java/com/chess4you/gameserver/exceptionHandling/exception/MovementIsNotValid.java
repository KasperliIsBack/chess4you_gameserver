package com.chess4you.gameserver.exceptionHandling.exception;

import com.chess4you.gameserver.data.movement.Movement;

public class MovementIsNotValid extends Exception {
    public MovementIsNotValid(Movement movement) {
        super(String.format("This Movement is not Valid! NewPosition: %s, OldPosition: %s, Direction: %s", movement.getNewPosition(), movement.getOldPosition(), movement.getDirection()));
    }
}
