package com.chess4you.gameserver.service.turnvalidationservice;

import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.service.TurnValidatorService;
import org.junit.Assert;
import org.junit.Test;

import static com.chess4you.gameserver.data.enums.Color.Black;
import static com.chess4you.gameserver.data.enums.Color.White;
import static org.hamcrest.CoreMatchers.is;

/*
Test for turnValidatorService:
    PieceOnStartPosition:
        - test Pawn
            - onPosition
            - notOnPosition
            - positionNotOnBoard
        - test King
            - onPosition
            - notOnPosition
            - positionNotOnBoard
        - test Rock
            - onPosition
            - notOnPosition
            - positionNotOnBoard
 */
public class TestsTurnValidatorServiceOfMethodOnStartPosition {

    TurnValidatorService turnValidatorService = new TurnValidatorService();

    @Test
    public void IfBlackPawnOnStartPosition() {
        Pawn pawn = new Pawn(Black);
        pawn.setPosition(new Position(0,6));

        boolean actualResult = turnValidatorService.onStartPosition(pawn);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfWhitePawnOnStartPosition() {
        Pawn pawn = new Pawn(White);
        pawn.setPosition(new Position(7, 1));

        boolean actualResult = turnValidatorService.onStartPosition(pawn);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfBlackPawnNotOnStartPosition() {
        Pawn pawn = new Pawn(Black);
        pawn.setPosition(new Position(7,7));

        boolean actualResult = turnValidatorService.onStartPosition(pawn);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhitePawnNotOnStartPosition() {
        Pawn pawn = new Pawn(White);
        pawn.setPosition(new Position(0, 0));

        boolean actualResult = turnValidatorService.onStartPosition(pawn);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfPawnNotOnBoard() {
        Pawn pawn = new Pawn(White);
        pawn.setPosition(new Position(18, 99));

        boolean actualResult = turnValidatorService.onStartPosition(pawn);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackKingOnStartPosition() {
        King king = new King(Black);
        king.setPosition(new Position(3, 0));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfWhiteKingOnStartPosition() {
        King king = new King(White);
        king.setPosition(new Position(3, 7));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfBlackKingNotOnStartPositionHorizontal() {
        King king = new King(White);
        king.setPosition(new Position(4, 0));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackKingNotOnStartPositionVertical() {
        King king = new King(White);
        king.setPosition(new Position(3, 1));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteKingNotOnStartPositionHorizontal() {
        King king = new King(White);
        king.setPosition(new Position(6, 0));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteKingNotOnStartPositionVertical() {
        King king = new King(White);
        king.setPosition(new Position(4, 1));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfKingNotOnBoard() {
        King king = new King(White);
        king.setPosition(new Position(18, 99));

        boolean actualResult = turnValidatorService.onStartPosition(king);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackRockOnStartPositionLeft() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(0,7));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfBlackRockOnStartPositionRight() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(7,7));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfWhiteRockOnStartPositionLeft() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(0, 0));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfWhiteRockOnStartPositionRight() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(7, 0));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void IfBlackRockNotOnStartPositionHorizontalLeft() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(1,7));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackRockNotOnStartPositionHorizontalRight() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(6,7));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackRockNotOnStartPositionVerticalLeft() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(7,6));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfBlackRockNotOnStartPositionVerticalRight() {
        Rock rock = new Rock(Black);
        rock.setPosition(new Position(0,6));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteRockNotOnStartPositionHorizontalLeft() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(1, 0));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteRockNotOnStartPositionHorizontalRight() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(6, 0));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteRockNotOnStartPositionVerticalLeft() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(0, 1));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfWhiteRockNotOnStartPositionVerticalRight() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(7, 1));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void IfRockNotOnBoard() {
        Rock rock = new Rock(White);
        rock.setPosition(new Position(18, 99));

        boolean actualResult = turnValidatorService.onStartPosition(rock);

        Assert.assertThat(actualResult, is(false));
    }
}
