package com.chess4you.gameserver.data;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Pawn;
import com.chess4you.gameserver.data.piece.Piece;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Type;
import java.util.*;

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
    private String mapPosPiece;

    public Map<Position, Piece> getMapPosPiece() {
        Type type = new TypeToken<ArrayList<Pawn>>(){}.getType();
        List<Pawn> listPiece = new Gson().fromJson(mapPosPiece, type);
        Map<Position, Piece> mapPosPiece = new HashMap<>();
        if(listPiece == null) {
            return mapPosPiece;
        }
        for (var piece : listPiece) {
            mapPosPiece.put(piece.getPosition(), piece);
        }
        return mapPosPiece;
    }

    public void setMapPosPiece(Map<Position, Piece> mapPosPiece) {
        List<Piece> listPiece = new ArrayList<>();
        listPiece.addAll(mapPosPiece.values());
        List<Position> listPosition = new ArrayList<>();
        listPosition.addAll(mapPosPiece.keySet());

        for (int index = 0; index < listPiece.size(); index++) {
            var piece = listPiece.get(index);
            piece.setPosition(listPosition.get(index));
            listPiece.set(index, piece);
        }
        this.mapPosPiece = new Gson().toJson(listPiece);
    }
}
