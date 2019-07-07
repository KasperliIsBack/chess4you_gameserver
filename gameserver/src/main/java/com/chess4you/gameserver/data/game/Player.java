package com.chess4you.gameserver.data.game;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Document(collection = "players")
@Data
@RequiredArgsConstructor
public class Player {
    @Id
    private String playerUuid;
    @NonNull @Size(max = 12) private String playerName;
}
