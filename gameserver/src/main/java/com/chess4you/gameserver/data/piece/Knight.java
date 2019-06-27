package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Position;

public class Knight extends Piece {
    public Knight(Color color, Position position) {
        super(color, position);;
        this.setDirections(new DirectionType[]{
                DirectionType.Knight
        });
        this.setType(PieceType.Knight);
    }
}
