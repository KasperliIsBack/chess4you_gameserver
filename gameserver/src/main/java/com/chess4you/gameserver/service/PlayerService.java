package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.game.Player;
import com.chess4you.gameserver.repository.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PlayerService {

    private HashMap<String, Player> ListPlayer = new HashMap<>();
    private IPlayerRepository playerRepository;

    @Autowired
    public PlayerService(IPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        for(Player player : this.playerRepository.findAll()){
            ListPlayer.put(player.getPlayerName(), player);
        }
    }

    public boolean playerExists(String playerName){
        if(ListPlayer.containsKey(playerName)){
            return true;
        } else {
            return false;
        }
    }

    public Player setPlayer(Player player) {
        ListPlayer.put(player.getPlayerName(), player);
        return playerRepository.insert(player);
    }

    public Player getPlayer(String playerName) {
        return ListPlayer.get(playerName);
    }
}
