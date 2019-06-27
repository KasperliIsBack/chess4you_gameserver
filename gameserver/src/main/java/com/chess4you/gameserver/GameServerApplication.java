package com.chess4you.gameserver;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Pawn;
import com.chess4you.gameserver.repository.IGameDataRepository;
import com.chess4you.gameserver.service.GameService;
import com.mongodb.BasicDBObject;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GameServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameServerApplication.class, args);
	}

}
