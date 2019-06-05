package com.chess4you.gameserver.data;

import com.chess4you.gameserver.data.movement.Movement;
import lombok.Data;

import java.util.List;

@Data
public class EnPasseResult {
    public boolean isEnPassePossible;
    public List<Movement> movements;

    public EnPasseResult(boolean isEnPassePossible) {
        this.isEnPassePossible = isEnPassePossible;
    }

    public EnPasseResult(boolean isEnPassePossible, List<Movement> movements) {
        this.isEnPassePossible = isEnPassePossible;
        this.movements = movements;
    }
}
