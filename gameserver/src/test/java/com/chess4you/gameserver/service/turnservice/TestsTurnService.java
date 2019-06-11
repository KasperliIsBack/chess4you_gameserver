package com.chess4you.gameserver.service.turnservice;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.*;
import com.chess4you.gameserver.service.TurnService;
import com.chess4you.gameserver.service.TurnValidatorService;
import lombok.var;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Hashtable;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
public class TestsTurnService {

    private TurnService turnService = new TurnService(new TurnValidatorService());

    @Test
    public void TestMovementsPawn() {
        // init used objects
        var pawn = new Pawn(Color.Black);
        pawn.setPosition(new Position(2, 1));
        var opponentPawnOne = new Pawn(Color.White);
        opponentPawnOne.setPosition(new Position(2, 2));
        var opponentPawnTwo = new Pawn(Color.White);
        opponentPawnTwo.setPosition(new Position(3, 1));

        var dicPosPiece = new Hashtable();
        dicPosPiece.put(pawn.getPosition(), pawn);
        dicPosPiece.put(opponentPawnOne.getPosition(), opponentPawnOne);
        dicPosPiece.put(opponentPawnTwo.getPosition(), opponentPawnTwo);

        var arrayOfMovementsExpected = new Movement[1];
        arrayOfMovementsExpected[0] = new Movement(new Position(3, 1), pawn.getPosition(), Direction.FREnPasse);

        // get data for testList
        var arrayOfMovementActual = turnService.getPossibleTurnFor(dicPosPiece, pawn);

        Assert.assertThat(arrayOfMovementActual.length, is(arrayOfMovementsExpected.length));
        AssertThatAllMovementsAreEqual(arrayOfMovementsExpected, arrayOfMovementActual);
    }

    @Test
    public void TestMovementsRock() {
        // init used objects
        var rock = new Rock(Color.Black);
        rock.setPosition(new Position(0, 0));
        var opponentPawnOne = new Rock(Color.White);
        opponentPawnOne.setPosition(new Position(7, 0));

        var dicPosPiece = new Hashtable();
        dicPosPiece.put(rock.getPosition(), rock);
        dicPosPiece.put(opponentPawnOne.getPosition(), opponentPawnOne);

        var arrayOfMovementsExpected = new Movement[14];
        arrayOfMovementsExpected[0] = new Movement(new Position(0, 1), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[1] = new Movement(new Position(0, 2), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[2] = new Movement(new Position(0, 3), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[3] = new Movement(new Position(0, 4), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[4] = new Movement(new Position(0, 5), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[5] = new Movement(new Position(0, 6), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[6] = new Movement(new Position(0, 7), rock.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[7] = new Movement(new Position(1, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[8] = new Movement(new Position(2, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[9] = new Movement(new Position(3, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[10] = new Movement(new Position(4, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[11] = new Movement(new Position(5, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[12] = new Movement(new Position(6, 0), rock.getPosition(), Direction.Right);
        arrayOfMovementsExpected[13] = new Movement(new Position(7, 0), rock.getPosition(), Direction.Right);

        // get data for testList
        var arrayOfMovementActual = turnService.getPossibleTurnFor(dicPosPiece, rock);

        Assert.assertThat(arrayOfMovementActual.length, is(arrayOfMovementsExpected.length));
        AssertThatAllMovementsAreEqual(arrayOfMovementsExpected, arrayOfMovementActual);
    }

    @Test
    public void TestMovementsBishop() {
        // init used objects
        var bishop = new Bishop(Color.Black);
        bishop.setPosition(new Position(0, 0));
        var opponentPawnOne = new Rock(Color.White);
        opponentPawnOne.setPosition(new Position(7, 7));

        var dicPosPiece = new Hashtable();
        dicPosPiece.put(bishop.getPosition(), bishop);
        dicPosPiece.put(opponentPawnOne.getPosition(), opponentPawnOne);

        var arrayOfMovementsExpected = new Movement[7];
        arrayOfMovementsExpected[0] = new Movement(new Position(1, 1), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[1] = new Movement(new Position(2, 2), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[2] = new Movement(new Position(3, 3), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[3] = new Movement(new Position(4, 4), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[4] = new Movement(new Position(5, 5), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[5] = new Movement(new Position(6, 6), bishop.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[6] = new Movement(new Position(7, 7), bishop.getPosition(), Direction.FRDiagonal);

        // get data for testList
        var arrayOfMovementActual = turnService.getPossibleTurnFor(dicPosPiece, bishop);

        Assert.assertThat(arrayOfMovementActual.length, is(arrayOfMovementsExpected.length));
        AssertThatAllMovementsAreEqual(arrayOfMovementsExpected, arrayOfMovementActual);
    }

    @Test
    public void TestMovementsQueen() {
        // init used objects
        var queen = new Queen(Color.Black);
        queen.setPosition(new Position(0, 0));
        var opponentPawnOne = new Rock(Color.White);
        opponentPawnOne.setPosition(new Position(7, 7));

        var dicPosPiece = new Hashtable();
        dicPosPiece.put(queen.getPosition(), queen);
        dicPosPiece.put(opponentPawnOne.getPosition(), opponentPawnOne);

        var arrayOfMovementsExpected = new Movement[21];
        arrayOfMovementsExpected[0] = new Movement(new Position(1, 1), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[1] = new Movement(new Position(2, 2), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[2] = new Movement(new Position(3, 3), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[3] = new Movement(new Position(4, 4), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[4] = new Movement(new Position(5, 5), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[5] = new Movement(new Position(6, 6), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[6] = new Movement(new Position(7, 7), queen.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[7] = new Movement(new Position(0, 1), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[8] = new Movement(new Position(0, 2), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[9] = new Movement(new Position(0, 3), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[10] = new Movement(new Position(0, 4), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[11] = new Movement(new Position(0, 5), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[12] = new Movement(new Position(0, 6), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[13] = new Movement(new Position(0, 7), queen.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[14] = new Movement(new Position(1, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[15] = new Movement(new Position(2, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[16] = new Movement(new Position(3, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[17] = new Movement(new Position(4, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[18] = new Movement(new Position(5, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[19] = new Movement(new Position(6, 0), queen.getPosition(), Direction.Right);
        arrayOfMovementsExpected[20] = new Movement(new Position(7, 0), queen.getPosition(), Direction.Right);

        // get data for testList
        var arrayOfMovementActual = turnService.getPossibleTurnFor(dicPosPiece, queen);

        Assert.assertThat(arrayOfMovementActual.length, is(arrayOfMovementsExpected.length));
        AssertThatAllMovementsAreEqual(arrayOfMovementsExpected, arrayOfMovementActual);
    }

    @Test
    public void TestMovementsKing() {
        // init used objects
        var king = new King(Color.Black);
        king.setPosition(new Position(0, 0));
        var opponentPawnOne = new Rock(Color.White);
        opponentPawnOne.setPosition(new Position(7, 7));

        var dicPosPiece = new Hashtable();
        dicPosPiece.put(king.getPosition(), king);
        dicPosPiece.put(opponentPawnOne.getPosition(), opponentPawnOne);

        var arrayOfMovementsExpected = new Movement[3];
        arrayOfMovementsExpected[0] = new Movement(new Position(1, 1), king.getPosition(), Direction.FRDiagonal);
        arrayOfMovementsExpected[1] = new Movement(new Position(0, 1), king.getPosition(), Direction.Forward);
        arrayOfMovementsExpected[2] = new Movement(new Position(1, 0), king.getPosition(), Direction.Right);

        // get data for testList
        var arrayOfMovementActual = turnService.getPossibleTurnFor(dicPosPiece, king);

        Assert.assertThat(arrayOfMovementActual.length, is(arrayOfMovementsExpected.length));
        AssertThatAllMovementsAreEqual(arrayOfMovementsExpected, arrayOfMovementActual);
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
