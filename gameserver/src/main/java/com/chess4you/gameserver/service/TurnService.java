package com.chess4you.gameserver.service;

import com.chess4you.gameserver.data.GameData;
import com.chess4you.gameserver.data.board.Field;
import com.chess4you.gameserver.data.movement.Movement;
import com.chess4you.gameserver.data.movement.Position;
import com.chess4you.gameserver.data.piece.Piece;
import lombok.var;
import org.chess4you.server.data.chessboard.board.ChessBoard;
import org.chess4you.server.data.chessboard.board.ChessEnum;
import org.chess4you.server.data.chessboard.movements.DiagonalMovement;
import org.chess4you.server.data.chessboard.movements.KnightMovement;
import org.chess4you.server.data.chessboard.movements.LinearMovement;
import org.chess4you.server.data.chessboard.movements.RochadeMovement;
import org.chess4you.server.data.chessboard.movements.base.Movement;
import org.chess4you.server.data.chessboard.movements.base.Position;
import org.chess4you.server.data.chessboard.pieces.PieceType;
import org.chess4you.server.data.game.Player;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TurnService {

    private TurnValidatorService validator = new TurnValidatorService();
    private LinearMovement linearMovements = new LinearMovement();
    private DiagonalMovement diagonalMovements = new DiagonalMovement();
    private KnightMovement knightMovements = new KnightMovement();
    private RochadeMovement rochadeMovement = new RochadeMovement();


    public List<Movement> getPossibleTurnFor(String lobbyUuid, String playerUuid, Position position) throws Exception {
        var game = gameService.getGame(UUID.fromString(lobbyUuid));
        var duration = game.getLastChange();
        duration.setMinutes(10);
        if(game.getLastChange().getTime() < gameService.getCurrentDate() &&
                gameService.getCurrentDate() > duration.getTime()) {
            if (game.getCurrentPlayer().getId() == playerUuid) {
                var listPieceType = game.getBoard().getListPosPieceType();
                var piece = listPieceType.get(position);
                if(piece != null){
                    throw new Exception();
                }
                tmpList =  turnHandler.getPossibleTurnFor(listPieceType, piece);
                return tmpList;
            } else {
                throw  new Exception();
            }
        } else {
            throw new Exception();
        }
    }

    public Field[][] doTurn(String lobbyUuid, String playerUuid, String tmpMovement) {
        var inList = tmpList.stream()
                .filter(movement ->  turnValidatorService.movementAreEqual(movement, tmpMovement))
                .findFirst();
        var lobby = lobbyHandler.getLobby(UUID.fromString(lobbyUuid));
        var game = gameService.getGame(UUID.fromString(lobbyUuid));
        if(inList != null){

            var duration = game.getLastChange();
            duration.setMinutes(10);
            if(game.getLastChange().getTime() < gameService.getCurrentDate() &&
                    gameService.getCurrentDate() > duration.getTime()) {
                if (game.getCurrentPlayer().getId() == playerUuid) {
                    var piece = Collections.list(game.getBoard().getListPosPieceType().elements())
                            .stream()
                            .filter(element -> element.getPosition() == inList.get().getOldPosition())
                            .findFirst();

                    game.getBoard().getListPosPieceType()
                            .put(inList.get().getNewPosition(), piece.get());

                    game.setLastChange(new Date());

                    if(lobby.getPlayerTwo().getId() == playerUuid) {
                        game.setCurrentPlayer(lobby.getPlayerOne());
                    } else {
                        game.setCurrentPlayer(lobby.getPlayerTwo());
                    }
                    game.setKingIsThreatenedResult(turnHandler.getPossibleThreadedPositionsForKing(game.getBoard(),game.getCurrentPlayer()));
                    return generateBoardForDoTurn(game, lobby);
                }
            }
        }
        return generateBoardForDoTurn(game, lobby);
    }

    private Field[][] generateBoardForDoTurn(Game game, Lobby lobby) {
        if(game.getCurrentPlayer().getId() == lobby.getPlayerTwo().getId()) {
            if(lobby.getPlayerTwo().getColor() == Color.White) {
                return game.getBoard().generateChessBoard(true);
            } else {
                return game.getBoard().generateChessBoard(false);
            }
        } else {
            if(lobby.getPlayerOne().getColor() ==  Color.White) {
                return game.getBoard().generateChessBoard(true);
            } else {
                return game.getBoard().generateChessBoard(false);
            }
        }
    }

    public Field[][] getBoardState(String lobbyUuid, String playerUuid) {
        var game =  gameService.getGame(UUID.fromString(lobbyUuid));
        if(game.getCurrentPlayer().getColor() == Color.White)  {
            return game.getBoard().generateChessBoard(true);
        } else {
            return game.getBoard().generateChessBoard(false);
        }
    }

    public Game startGame(String lobbyUuid) {
        var lobby = lobbyHandler.getLobby(UUID.fromString(lobbyUuid));
        if(lobby != null && lobby.getPlayerTwo() != null) {
            gameService.setGame(lobby);
        }
        return gameService.getGame(UUID.fromString(lobbyUuid));
    }


    public List<Movement> getPossibleTurnFor(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        var listMovements = new ArrayList<Movement>();
        for (int i = 0; i < piece.getDirections().length; i++) {
          switch (piece.getDirections()[i]) {
              case Linear:
                  listMovements.addAll(linearMovements(listPieceType, piece));
                  break;
              case Diagonal:
                  listMovements.addAll(diagonalMovements(listPieceType, piece));
                  break;
              case Pawn:
                  listMovements.addAll(enPasse(listPieceType, piece, true));
                  break;
              case Knight:
                  listMovements.addAll(knightMovements(listPieceType, piece));
                  break;
              case Rochade:
                  listMovements.addAll(rochadeMovements(listPieceType, piece));
          }
        }
        return listMovements;
    }

    private List<Movement> rochadeMovements(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        if(piece.getType() == PieceType.Knight) {
            var tmp = new ArrayList<Movement>();
            if(validator.rochadePossible(listPieceType, piece)) {
                var indexRock =  Collections.list(listPieceType.keys())
                        .stream()
                        .filter(position ->
                                position.getPosY() == piece.getPosition().getPosY()
                                        && position.getPosX() != piece.getPosition().getPosX()
                        )
                        .collect(Collectors.toList());
                var rock = listPieceType.get(indexRock.get(0));
                switch (validator.rochadeType(listPieceType, piece)) {
                    case smallRochade:
                        tmp.addAll(Arrays.asList(rochadeMovement.smallRochade(piece.getPosition(), rock.getPosition())));
                        break;
                    case bigRochade:
                        tmp.addAll(Arrays.asList(rochadeMovement.bigRochade(piece.getPosition(), rock.getPosition())));
                        break;
                }
            }
            return tmp;
        } else {
            return null;
        }
    }

    public List<Movement> diagonalMovements(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        int number;
        switch (piece.getType()){
            case King:
                number = 1;
                break;
            default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(var method : diagonalMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, listPieceType, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> linearMovements(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        int number;
        switch (piece.getType()){
            case King:
                number = 1;
                break;
            case Pawn:
                number = validator.onStartPosition(piece) ? 2 : 1;
            default:
                number = 8;
                break;
        }
        var tmp = new ArrayList<Movement>();
        for(var method : linearMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, listPieceType, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> enPasse(Dictionary<Position, PieceType> listPieceType, PieceType piece, boolean reverse) {
        var tmp = new ArrayList<Movement>();
        if(reverse) {
            var result = validator.enPassePossible(listPieceType, piece);
            if(result.isEnPassePossible) {
                for(var position :  Collections.list(listPieceType.keys())) {
                    for(var movement : result.getMovements()) {
                        if(position.getPosY() == movement.getOldPosition().getPosY()
                        && position.getPosX() == movement.getOldPosition().getPosX()) {
                            if(validator.onStartPosition(listPieceType.get(position))) {
                                tmp.add(movement);
                            }
                        }
                    }
                }
            }
        } else {
            if(validator.onStartPosition(piece)) {
                var result = validator.enPassePossible(listPieceType, piece);
                if(result.isEnPassePossible) {
                    tmp.addAll(result.movements);
                }
            }
        }
        return tmp;
    }

    public List<Movement> knightMovements(Dictionary<Position, PieceType> listPieceType, PieceType piece) {
        int number = 1;
        var tmp = new ArrayList<Movement>();
        for(var method : knightMovements.getClass().getMethods()){
            if(method.getReturnType() == Movement.class) {
                tmp.addAll(movementsGeneral(diagonalMovements, method, 0, number, listPieceType, piece, new ArrayList<>()));
            } else {
                break;
            }
        }
        return tmp;
    }

    public List<Movement> movementsGeneral(Object instance, Method method, int counter, int number, Dictionary<Position, PieceType> listPieceType, PieceType piece, List<Movement> listMovements){
        counter = counter == 0 ? 1 : counter;
        try{
            Object tmp = number == 0 ? method.invoke(instance, piece.getPosition()) :
                    method.invoke(instance, piece.getPosition(), counter);
            Movement movement = (Movement) tmp;
           var type = validator.pieceOnPosition(listPieceType, movement.getNewPosition(), piece);
            switch (type) {
                case Friendly:
                    return listMovements;
                case Enemeny:
                    listMovements.add(movement);
                    return listMovements;
                case Nothing:
                    if(validator.possibleMovementOnBoard(movement)) {
                        listMovements.add(movement);
                        if(counter < number){
                            movementsGeneral(instance, method, ++counter, number, listPieceType, piece, listMovements);
                        }
                        return listMovements;
                    }
                    return  listMovements;
            }
            return listMovements;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public List<Movement> getPossibleThreadedPositionsForKing(ChessBoard chessBoard, Player player) {
        PieceType king = chessBoard.getKingOfOpponent(player);
        var listMovements = new ArrayList<Movement>();
        Dictionary<Position, PieceType> listPosPieceType = chessBoard.getListPosPieceType();
        listPosPieceType.remove(king);
        for (int i = 0; i < king.getDirections().length; i++) {
            switch (king.getDirections()[i]) {
                case Linear:
                    listMovements.addAll(linearMovements(listPosPieceType, king));
                    break;
                case Diagonal:
                    listMovements.addAll(diagonalMovements(listPosPieceType, king));
                    break;
                case Pawn:
                    listMovements.addAll(enPasse(listPosPieceType, king, true));
                    break;
                case Knight:
                    listMovements.addAll(knightMovements(listPosPieceType, king));
                    break;
                case Rochade:
                    listMovements.addAll(rochadeMovements(listPosPieceType, king));
            }
        }
        return listMovements;
    }

    public Movement[] getPossibleTurnFor(Dictionary<Position, Piece> dicPosPiece, Piece pieceOnThatPosition) {
        return new Movement[0];
    }

    public GameData doTurn(GameData gameData, Movement movement) {
        var dicPosPiece = gameData.getDicPosPiece();
        var piece = dicPosPiece.get(movement.getOldPosition());
        if(Collections.list(dicPosPiece.keys()).contains(movement.getNewPosition())) {
            dicPosPiece.remove(movement.getNewPosition());
        }

        dicPosPiece.put(movement.getNewPosition(), piece);
        gameData.setDicPosPiece(dicPosPiece);
        return gameData;
    }
}
