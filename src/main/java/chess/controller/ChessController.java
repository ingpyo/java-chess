package chess.controller;

import chess.domain.ChessGame;
import chess.domain.PiecesPosition;
import chess.domain.position.Position;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.ViewFile;
import chess.view.ViewRank;
import java.util.List;
import java.util.function.Supplier;

public class ChessController {

    private static final int COMMAND_INDEX = 0;
    private static final int FROM_POSITION_INDEX = 1;
    private static final int TO_POSITION_INDEX = 2;

    private final OutputView outputView;
    private final InputView inputView;

    public ChessController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        outputView.printStartPrefix();
        ChessCommand chessCommand = retryOnInvalidUserInput(() -> {
            List<String> strings = inputView.readCommand();
            return ChessCommand.getStart(strings.get(COMMAND_INDEX));
        });

        ChessGame chessGame = startChessGame();
        play(chessGame, chessCommand);
    }

    private ChessGame startChessGame() {
        PiecesPosition piecesPosition = new PiecesPosition();
        printBoard(piecesPosition);

        return new ChessGame(piecesPosition);
    }

    private void play(ChessGame chessGame, ChessCommand command) {
        do {
            command = retryOnInvalidUserInput(() -> playTurn(chessGame));
        } while (!command.isEnd());
    }

    private ChessCommand playTurn(ChessGame chessGame) {
        List<String> commands = inputView.readCommand();
        ChessCommand chessCommand = ChessCommand.getPlayingCommand(commands.get(COMMAND_INDEX));
        if (chessCommand.isEnd()) {
            return chessCommand;
        }

        move(chessGame, commands);
        printBoard(chessGame.getPiecesPosition());
        return chessCommand;
    }

    private void move(ChessGame chessGame, List<String> commands) {
        String fromInput = commands.get(FROM_POSITION_INDEX);
        String toInput = commands.get(TO_POSITION_INDEX);

        chessGame.move(toPosition(fromInput), toPosition(toInput));
    }

    private Position toPosition(String positionInput) {
        String fileInput = String.valueOf(positionInput.charAt(0));
        String rankInput = String.valueOf(positionInput.charAt(FROM_POSITION_INDEX));

        return new Position(ViewFile.from(fileInput), ViewRank.from(rankInput));
    }

    public void printBoard(PiecesPosition piecesPosition) {
        outputView.printChessState(piecesPosition.getPiecesPosition());
    }

    private <T> T retryOnInvalidUserInput(Supplier<T> request) {
        try {
            return request.get();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return retryOnInvalidUserInput(request);
        }
    }
}
