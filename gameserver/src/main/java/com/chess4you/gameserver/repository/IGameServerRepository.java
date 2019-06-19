package com.chess4you.gameserver.repository;

import com.chess4you.gameserver.data.GameServer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGameServerRepository extends MongoRepository<GameServer, String> {
}
