package com.chess4you.gameserver.data.game;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.piece.Piece;
import com.google.gson.Gson;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Movement[] historyOfMovementData;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) private Piece[] arrayOfPiece;

    public Map<String, Piece> getMapPosPiece() {
        var gson = new Gson();
        Map<String, Piece> mapPosPiece = new HashMap<>();
        if(arrayOfPiece != null) {
            for (int index = 0; index < arrayOfPiece.length; index++) {
                mapPosPiece.put(gson.toJson(arrayOfPiece[index].getPosition()), arrayOfPiece[index]);
            }
        }
        return mapPosPiece;
    }

    public void setMapPosPiece(Map<String, Piece> mapPosPiece) {
        List<Piece> tmpPieceList = new ArrayList<>(mapPosPiece.values());
        arrayOfPiece = new Piece[tmpPieceList.size()];
        for (int index = 0; index < tmpPieceList.size(); index++) {
            arrayOfPiece[index] = tmpPieceList.get(index);
        }
    }
}
