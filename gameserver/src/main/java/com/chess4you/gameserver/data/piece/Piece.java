package com.chess4you.gameserver.data.piece;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.DirectionType;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Position;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Data
public abstract class Piece {
    @Setter(AccessLevel.NONE)
    private UUID pieceUuid = UUID.randomUUID();
    @Setter(AccessLevel.NONE)
    @NonNull private Color color;
    private String pieceName;
    private Position position;
    private PieceType type;
    private DirectionType[] directions;
    public Piece(Color color){
        pieceName = this.getClass().getName() + color.toString();
        this.color = color;
    }
}

