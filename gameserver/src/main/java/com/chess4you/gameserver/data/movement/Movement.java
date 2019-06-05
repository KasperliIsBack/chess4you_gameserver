package com.chess4you.gameserver.data.movement;

import com.chess4you.gameserver.data.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Movement {
    private Position newPosition;
    private Position oldPosition;
    private Direction direction;
}
