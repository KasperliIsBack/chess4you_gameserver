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
import java.util.Dictionary;

@Document(collection = "gameData")
@Data
@RequiredArgsConstructor
public class GameData {
    @Id private String UuidGame;
    @NonNull private String GameName;
    @NonNull private Player FirstPlayer;
    @NonNull private Color colorFirstPlayer;
    private boolean IsFirstPlayerConnected;
    @NonNull private Player SecondPlayer;
    @NonNull private Color colorSecondPlayer;
    private boolean IsSecondPlayerConnected;
    @NonNull private Player CurrentPlayer;
    private int GamePeriodInMinute;
    private Date TurnDate;
    private Movement[] HistoryOfMovements;
    private Dictionary<Position, Piece> DicPosPiece;
}
