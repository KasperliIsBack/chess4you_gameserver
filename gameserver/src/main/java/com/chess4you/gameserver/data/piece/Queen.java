package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Diagonal,
                DirectionType.Linear,
        });
        this.setType(PieceType.Queen);
    }
}
