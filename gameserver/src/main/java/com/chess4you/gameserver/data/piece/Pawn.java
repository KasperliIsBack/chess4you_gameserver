package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
        this.setDirections(new DirectionType[]{
                DirectionType.Pawn,
                DirectionType.Linear,
        });
        this.setType(PieceType.Pawn);
    }
}
