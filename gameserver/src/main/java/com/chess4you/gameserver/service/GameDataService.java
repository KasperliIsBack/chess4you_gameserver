package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.game.GameData;
import com.chess4you.gameserver.exceptionHandling.exception.GameDataNotAvailable;
import com.chess4you.gameserver.repository.IGameDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameDataService {
    private IGameDataRepository gameDataRepository;

    @Autowired
    public GameDataService(IGameDataRepository gameRepository) {
        this.gameDataRepository = gameRepository;
    }

    public GameData getGameData(String gameDataUuid) throws Exception {
        if(gameDataRepository.existsById(gameDataUuid)) {
            Optional<GameData> gameData = gameDataRepository.findById(gameDataUuid);
            if(gameData.isPresent()) {
                return gameData.get();
            } else {
                throw new GameDataNotAvailable(gameDataUuid);
            }
        } else {
            throw new GameDataNotAvailable(gameDataUuid);
        }
    }

    public GameData updateGameData(GameData gameData) throws Exception {
        if(gameDataRepository.existsById(gameData.getGameUuid())) {
            return gameDataRepository.save(gameData);
        } else {
            throw new GameDataNotAvailable(gameData.getGameUuid());
        }
    }
}
