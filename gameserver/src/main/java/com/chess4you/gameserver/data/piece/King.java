package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Position;

public class King extends Piece {
    public King(Color color, Position position) {
        super(color, position);
        this.setDirections(new DirectionType[]{
                DirectionType.Diagonal,
                DirectionType.Linear,
                DirectionType.Rochade,
        });
        this.setType(PieceType.King);
    }
}
