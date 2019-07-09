package com.chess4you.gameserver.service.turnservice;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.enums.PieceType;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import com.chess4you.gameserver.service.TurnService;
import com.chess4you.gameserver.service.TurnValidatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
public class TestsTurnService {

    private TurnService turnService = new TurnService(new TurnValidatorService());

    @Test
    public void TestStartMovementPawn(){
        Direction[] directions = new Direction[]{ Direction.Forward, Direction.FLEnPasse, Direction.FREnPasse};
        Piece pawn = new Piece(PieceType.Pawn, directions , Color.Black, new Position(1,1));
        Map<Position, Piece> mapPosPiece = new HashMap<>();
        mapPosPiece.put(pawn.getPosition(), pawn);

        // Movement[] arrayOfMovementsActual = turnService.getPossibleTurnFor(mapPosPiece, pawn, false);
        List<Integer> list = recursiveTest(new ArrayList<>(), 0);

        Assert.assertThat(list.size(), is(10));

    }


    public List<Integer> recursiveTest(List<Integer> list, int counter){
        if( counter < 10) {
            list.add(counter);
            recursiveTest(list, ++counter);
        }
        return list;
    }

    public void AssertThatAllMovementsAreEqual(Movement[] arrayOfMovementsCorrect, Movement[] arrayOfMovementsActual) {
        for (int index = 0; index < arrayOfMovementsCorrect.length; index++) {
            Assert.assertThat(arrayOfMovementsActual[index].getNewPosition().getPosY(), is(arrayOfMovementsCorrect[index].getNewPosition().getPosY()));
            Assert.assertThat(arrayOfMovementsActual[index].getNewPosition().getPosX(), is(arrayOfMovementsCorrect[index].getNewPosition().getPosX()));
            Assert.assertThat(arrayOfMovementsActual[index].getOldPosition().getPosY(), is(arrayOfMovementsCorrect[index].getOldPosition().getPosY()));
            Assert.assertThat(arrayOfMovementsActual[index].getOldPosition().getPosX(), is(arrayOfMovementsCorrect[index].getOldPosition().getPosX()));
            Assert.assertThat(arrayOfMovementsActual[index].getDirection(), is(arrayOfMovementsCorrect[index].getDirection()));
        }
    }
}
