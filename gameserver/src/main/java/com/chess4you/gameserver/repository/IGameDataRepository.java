package com.chess4you.gameserver.repository;

import com.chess4you.gameserver.data.GameData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGameDataRepository extends MongoRepository<GameData, String> {
}
