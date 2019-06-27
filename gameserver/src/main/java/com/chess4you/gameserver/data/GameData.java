package com.chess4you.gameserver.data;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "gameData")
@Data
@RequiredArgsConstructor
public class GameData {
    @Id private String gameUuid;
    @NonNull private String gameName;
    @NonNull private Player firstPlayer;
    @NonNull private Color colorFirstPlayer;
    private boolean isFirstPlayerConnected;
    @NonNull private Player secondPlayer;
    @NonNull private Color colorSecondPlayer;
    private boolean isSecondPlayerConnected;
    @NonNull private Player currentPlayer;
    private int gamePeriodInMinute;
    private Date turnDate;
    private Movement[] historyOfMovements;
    private Map<Position, Piece> mapPosPiece;
}
