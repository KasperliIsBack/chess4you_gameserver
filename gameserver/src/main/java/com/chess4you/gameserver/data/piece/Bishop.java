package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Position;

public class Bishop extends Piece {
    public Bishop(Color color, Position position) {
        super(color, position);
        this.setDirections(new DirectionType[]{
                DirectionType.Diagonal,
        });
        this.setType(PieceType.Bishop);
    }
}
