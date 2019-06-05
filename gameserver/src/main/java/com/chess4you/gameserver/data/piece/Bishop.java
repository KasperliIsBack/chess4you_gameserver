package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Linear,
        });
        this.setType(PieceType.Bishop);
    }
}
