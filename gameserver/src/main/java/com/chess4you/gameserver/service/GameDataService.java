package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
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

    public GameData getGameData(String gameUuid, String currentPlayerUuid) {
        if(gameDataRepository.existsById(gameUuid)) {
            if(isPlayerOnTurn(gameUuid, currentPlayerUuid)) {
                return gameDataRepository.findById(gameUuid).get();
            } else {
                // Todo throw Exception not valid player
                return null;
            }
        } else {
            // Todo throw Exception no game available
            return null;
        }
    }

    public GameData updateGameData(GameData gameData) {
        if(gameDataRepository.existsById(gameData.getGameUuid())) {
            return gameDataRepository.save(gameData);
        } else {
            // Todo throw Exception no game available
            return null;
        }
    }

    private boolean isPlayerOnTurn(String gameUuid, String currentPlayerUuid) {
        Optional<GameData> gameData = gameDataRepository.findById(gameUuid);
        return gameData.get().getCurrentPlayer().getPlayerUuid() == currentPlayerUuid ? true : false;
    }
}
