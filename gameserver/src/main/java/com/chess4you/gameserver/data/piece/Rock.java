package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class Rock extends Piece {
    public Rock(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Linear,
        });
        this.setType(PieceType.Rock);
    }
}
