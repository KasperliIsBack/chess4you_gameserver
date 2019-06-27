package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Position;

public class EmptyPiece extends Piece {
    public EmptyPiece(Color color, Position position) {
        super(color, position);
    }
}
