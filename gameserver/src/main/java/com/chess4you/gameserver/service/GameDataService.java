package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.repository.IGameDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameDataService {
    private IGameDataRepository gameRepository;

    @Autowired
    public GameDataService(IGameDataRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameData getGameData(String gameUuid, String currentPlayerUuid) {
        if(gameRepository.existsById(gameUuid)) {
            if(isPlayerOnTurn(gameUuid, currentPlayerUuid)) {
                return gameRepository.findById(gameUuid).get();
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
        if(gameRepository.existsById(gameData.getUuidGame())) {
            return gameRepository.save(gameData);
        } else {
            // Todo throw Exception no game available
            return null;
        }
    }

    private boolean isPlayerOnTurn(String gameUuid, String currentPlayerUuid) {
        Optional<GameData> gameData = gameRepository.findById(gameUuid);
        return gameData.get().getCurrentPlayer().getPlayerUUID() == currentPlayerUuid ? true : false;
    }
}
