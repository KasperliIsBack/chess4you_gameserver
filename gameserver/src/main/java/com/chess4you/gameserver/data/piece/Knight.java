package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Knight
        });
        this.setType(PieceType.Knight);
    }
}
