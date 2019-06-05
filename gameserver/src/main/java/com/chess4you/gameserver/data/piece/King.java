package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class King extends Piece {
    public King(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Rochade,
                DirectionType.Linear,
                DirectionType.Diagonal,
        });
        this.setType(PieceType.King);
    }
}
