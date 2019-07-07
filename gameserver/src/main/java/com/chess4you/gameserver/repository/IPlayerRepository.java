package com.chess4you.gameserver.repository;

import com.chess4you.gameserver.data.game.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlayerRepository extends MongoRepository<Player, String> {
}
