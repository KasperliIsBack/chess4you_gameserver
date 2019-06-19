package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameServer;
import com.chess4you.gameserver.repository.IGameServerRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GameServerService {
    private IGameServerRepository gameServerRepository;
    private org.slf4j.Logger logger;
    private final String host = "172.16.1.198";
    private final int port = 8081;

    @Autowired
    public GameServerService(IGameServerRepository gameRepository, org.slf4j.Logger logger) {
        this.gameServerRepository = gameRepository;
        this.logger = logger;
    }

    public void registerGameServer() {
        String serverName = "GameServer" + new Date().toString();
        GameServer gameServer = new GameServer(serverName, host, port);
        gameServerRepository.insert(gameServer);
        logger.info("Create GameServe: " + new Gson().toJson(gameServer));
    }
}
