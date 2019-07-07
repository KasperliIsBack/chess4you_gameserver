package com.chess4you.gameserver.repository;

import com.chess4you.gameserver.data.game.GameData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameDataRepository extends MongoRepository<GameData, String> {
}
