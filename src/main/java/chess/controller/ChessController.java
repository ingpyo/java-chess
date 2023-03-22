package chess.controller;

import chess.domain.ChessBoard;
import chess.domain.ChessBoardMaker;
import chess.domain.ChessGame;
import chess.domain.Turn;
import chess.domain.piece.Camp;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.view.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ChessController {

    private static final int COMMAND_INDEX = 0;
    private static final int FROM_POSITION_INDEX = 1;
    private static final int TO_POSITION_INDEX = 2;
    private static final int FILE_INDEX = 0;
    private static final int RANK_INDEX = 1;

    private final Map<ChessCommand, GameAction> commandMapper = new EnumMap<>(ChessCommand.class);
    private final OutputView outputView;
    private final InputView inputView;
    private final ChessGame game;

    public ChessController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.game = new ChessGame(ChessBoardMaker.create(), new Turn(Camp.WHITE));
        initController();
    }

    private void initController() {
        commandMapper.put(ChessCommand.START, this::start);
        commandMapper.put(ChessCommand.MOVE, this::move);
        commandMapper.put(ChessCommand.END, ignored -> {
        });
    }

    public void run() {
        outputView.printStartPrefix();
        ChessCommand gameCommand = ChessCommand.WAIT;
        while (gameCommand != ChessCommand.END) {
            gameCommand = play();
        }
    }

    private ChessCommand play() {
        try {
            List<String> commands = inputView.readCommand();
            ChessCommand command = ChessCommand.from(commands.get(COMMAND_INDEX));
            GameAction gameAction = commandMapper.get(command);
            gameAction.execute(commands);
            return command;
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            return ChessCommand.WAIT;
        }
    }

    private void start(List<String> commands) {
        ChessCommand.validateStartCommand(commands);
        printBoard(game.getChessBoard());
    }


    private void move(List<String> commands) {
        ChessCommand.validatePlayingCommand(commands);
        String fromInput = commands.get(FROM_POSITION_INDEX);
        String toInput = commands.get(TO_POSITION_INDEX);
        game.move(toPosition(fromInput), toPosition(toInput), new Path());
        printBoard(game.getChessBoard());
    }

    private Position toPosition(String positionInput) {
        String fileInput = String.valueOf(positionInput.charAt(FILE_INDEX));
        String rankInput = String.valueOf(positionInput.charAt(RANK_INDEX));

        return Position.of(ViewFile.from(fileInput), ViewRank.from(rankInput));
    }

    private void printBoard(ChessBoard chessBoard) {
        outputView.printChessState(chessBoard.getBoard());
    }
}
