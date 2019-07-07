package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.game.GameServer;
import com.chess4you.gameserver.repository.IGameServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GameServerService {
    private IGameServerRepository gameServerRepository;
    private final String host = "172.16.1.198";
    private final int port = 8081;

    @Autowired
    public GameServerService(IGameServerRepository gameRepository) {
        this.gameServerRepository = gameRepository;
    }

    public void registerGameServer() {
        String serverName = "GameServer" + new Date().toString();
        GameServer gameServer = new GameServer(serverName, host, port, false);
        gameServerRepository.insert(gameServer);
    }
}
