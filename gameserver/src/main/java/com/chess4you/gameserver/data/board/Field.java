package com.chess4you.gameserver.data.board;

import com.chess4you.gameserver.data.piece.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Field {
    private Piece piece;
    @NonNull private Boolean isActive;
}
