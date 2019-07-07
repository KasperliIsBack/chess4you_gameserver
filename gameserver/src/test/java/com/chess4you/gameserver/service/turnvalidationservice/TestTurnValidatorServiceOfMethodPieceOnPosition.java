package com.chess4you.gameserver.service.turnvalidationservice;

import com.chess4you.gameserver.data.enums.Color;
import com.chess4you.gameserver.data.enums.PositionType;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import com.chess4you.gameserver.service.TurnValidatorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Dictionary;
import java.util.Hashtable;

import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class TestTurnValidatorServiceOfMethodPieceOnPosition {

    private TurnValidatorService turnValidatorService = new TurnValidatorService();
    private Dictionary<Position, Piece> listPosPiece;

    public TestTurnValidatorServiceOfMethodPieceOnPosition() {
        listPosPiece = new Hashtable<>();
    }

    @Before
    public void init(){
        Pawn friendlyPawn = new Pawn(Color.Black);
        friendlyPawn.setPosition(new Position(4,5));
        Pawn hostilePawn = new Pawn(Color.White);
        hostilePawn.setPosition(new Position(6,1));

        listPosPiece.put(friendlyPawn.getPosition(), friendlyPawn);
        listPosPiece.put(hostilePawn.getPosition(), hostilePawn);
    }

    @Test
    public void IfFriendlyPieceOnPosition() {
        Pawn pawn = new Pawn(Color.Black);
        pawn.setPosition(new Position(0,6));

        PositionType actualPositionType = turnValidatorService.pieceOnPosition(listPosPiece,
                new Position(4,5), pawn );

        Assert.assertThat(actualPositionType, is(PositionType.Friendly));
    }

    @Test
    public void IfEnemyOnPosition() {
        Pawn pawn = new Pawn(Color.Black);
        pawn.setPosition(new Position(0,6));

        PositionType actualPositionType = turnValidatorService.pieceOnPosition(listPosPiece,
                new Position(6,1), pawn );

        Assert.assertThat(actualPositionType, is(PositionType.Enemy));
    }

    @Test
    public void IfNothingOnPosition() {
        Pawn pawn = new Pawn(Color.Black);
        pawn.setPosition(new Position(0,6));

        PositionType actualPositionType = turnValidatorService.pieceOnPosition(listPosPiece,
                new Position(1,0), pawn );

        Assert.assertThat(actualPositionType, is(PositionType.Nothing));
    }
}
