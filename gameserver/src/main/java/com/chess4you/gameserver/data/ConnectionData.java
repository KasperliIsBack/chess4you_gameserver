package com.chess4you.gameserver.data;

import lombok.Data;

@Data
public class ConnectionData {
    private String gameUuid;
    private String gameDataUuid;
    private String playerUuid;
}
