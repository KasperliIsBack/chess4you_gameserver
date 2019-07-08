package com.chess4you.gameserver.data.game;

import com.chess4you.gameserver.data.movement.Movement;
import lombok.Data;

@Data
public class TurnDto {
    private ConnectionData cnData;
    private Movement movement;
}
