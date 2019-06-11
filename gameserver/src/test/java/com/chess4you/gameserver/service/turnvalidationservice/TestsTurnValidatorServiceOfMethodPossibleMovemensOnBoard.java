package com.chess4you.gameserver.service.turnvalidationservice;

import com.chess4you.gameserver.data.enums.Direction;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.service.TurnValidatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class TestsTurnValidatorServiceOfMethodPossibleMovemensOnBoard {

    private TurnValidatorService turnValidatorService = new TurnValidatorService();

    @Test
    public void OnBoardLeftForward(){
        Movement movement = new Movement(new Position(0,7),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void OnBoardRightForward(){
        Movement movement = new Movement(new Position(7,7),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void OnBoardLeftBackward(){
        Movement movement = new Movement(new Position(0,0),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void OnBoardRightBackward(){
        Movement movement = new Movement(new Position(7,7),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(true));
    }

    @Test
    public void NotOnBoardLeftForward(){
        Movement movement = new Movement(new Position(-1,8),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void NotOnBoardRightForward(){
        Movement movement = new Movement(new Position(8,8),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void NotOnBoardLeftBackward(){
        Movement movement = new Movement(new Position(-5,-1),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(false));
    }

    @Test
    public void NotOnBoardRightBackward(){
        Movement movement = new Movement(new Position(14,-1),
                new Position(0, 0), Direction.FRDiagonal);

        boolean actualResult = turnValidatorService.possibleMovementOnBoard(movement);

        Assert.assertThat(actualResult, is(false));
    }
}
