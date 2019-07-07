package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.enums.DirectionType;
import lombok.Data;

@Data
public class PieceProperties {
    DirectionType[] directionTypes;
    Direction[] directions;
}
