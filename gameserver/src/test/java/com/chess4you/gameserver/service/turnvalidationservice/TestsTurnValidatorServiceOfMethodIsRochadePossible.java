package com.chess4you.gameserver.service.turnvalidationservice;

import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.King;
import com.chess4you.gameserver.data.piece.Pawn;
import com.chess4you.gameserver.data.piece.Piece;
import com.chess4you.gameserver.data.piece.Rock;
import com.chess4you.gameserver.service.TurnValidatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Dictionary;
import java.util.Hashtable;

import static com.chess4you.gameserver.data.enums.Color.Black;
import static com.chess4you.gameserver.data.enums.Color.White;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class TestsTurnValidatorServiceOfMethodIsRochadePossible {

    private TurnValidatorService turnValidatorService = new TurnValidatorService();

    @Test
    public void IsSmallWhiteRochadePossible() {
        Dictionary<Position, Piece> listRochade = new Hashtable<>();
        Rock rockWhiteLeft = new Rock(White);
        King king = new King(White);

        rockWhiteLeft.setPosition(new Position(0,0));
        king.setPosition(new Position(3,0));
        listRochade.put(rockWhiteLeft.getPosition(), rockWhiteLeft);
        listRochade.put(king.getPosition(), king);

        boolean actualResult = turnValidatorService.isRochadePossible(listRochade, king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IsBigWhiteRochadePossible() {
        Dictionary<Position, Piece> listRochade = new Hashtable<>();
        Rock rockWhiteRight = new Rock(White);
        King king = new King(White);

        rockWhiteRight.setPosition(new Position(7,0));
        king.setPosition(new Position(3,0));
        listRochade.put(rockWhiteRight.getPosition(), rockWhiteRight);
        listRochade.put(king.getPosition(), king);

        boolean actualResult = turnValidatorService.isRochadePossible(listRochade, king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IsSmallBlackRochadePossible() {
        Dictionary<Position, Piece> listRochade = new Hashtable<>();
        Rock rockBlackLeft = new Rock(Black);
        King king = new King(Black);

        rockBlackLeft.setPosition(new Position(0,7));
        king.setPosition(new Position(3,7));
        listRochade.put(rockBlackLeft.getPosition(), rockBlackLeft);
        listRochade.put(king.getPosition(), king);

        boolean actualResult = turnValidatorService.isRochadePossible(listRochade, king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IsBigBlackRochadePossible() {
        Dictionary<Position, Piece> listRochade = new Hashtable<>();
        Rock rockBlackRight = new Rock(Black);
        King king = new King(Black);

        rockBlackRight.setPosition(new Position(7,7));
        king.setPosition(new Position(3,7));
        listRochade.put(rockBlackRight.getPosition(), rockBlackRight);
        listRochade.put(king.getPosition(), king);

        boolean actualResult = turnValidatorService.isRochadePossible(listRochade, king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IsRochadeNotPossible() {
        Dictionary<Position, Piece> listRochade = new Hashtable<>();
        Rock rockWhiteLeft = new Rock(White);
        King king = new King(White);
        Pawn pawn = new Pawn(White);

        rockWhiteLeft.setPosition(new Position(0,0));
        pawn.setPosition(new Position(4, 0));
        king.setPosition(new Position(3,0));
        listRochade.put(rockWhiteLeft.getPosition(), rockWhiteLeft);
        listRochade.put(pawn.getPosition(), pawn);
        listRochade.put(king.getPosition(), king);

        boolean actualResult = turnValidatorService.isRochadePossible(listRochade, king);

        Assert.assertThat(actualResult, is(false));
    }
}
