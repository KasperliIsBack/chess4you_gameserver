package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Position;

public class KingIsThreatened extends Piece {
    public KingIsThreatened(Color color, Position position) {
        super(color, position);
        this.setDirections(new DirectionType[]{
                DirectionType.Rochade,
                DirectionType.Linear,
                DirectionType.Diagonal,
                DirectionType.Pawn,
                DirectionType.Knight,
                DirectionType.Diagonal,
        });
        this.setType(PieceType.King);
    }
}
