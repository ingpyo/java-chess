package chess.controller;

import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.piece.Camp;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.view.InputView;
import chess.view.OutputView;
import chess.view.ViewFile;
import chess.view.ViewRank;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ChessController {

    private static final int COMMAND_INDEX = 0;
    private static final int FROM_POSITION_INDEX = 1;
    private static final int FILE_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int TO_POSITION_INDEX = 2;

    private final OutputView outputView;
    private final InputView inputView;

    public ChessController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        outputView.printStartPrefix();
        requestStartCommand();

        ChessGame chessGame = startChessGame();
        play(chessGame);
    }

    private void requestStartCommand() {
        retryOnInvalidUserInput(() -> {
            List<String> strings = inputView.readCommand();
            return ChessCommand.getStart(strings.get(COMMAND_INDEX));
        });
    }

    private ChessGame startChessGame() {
        ChessBoard piecesPosition = new ChessBoard();
        Map<Position, Piece> chessBoard = piecesPosition.createBoard();

        printBoard(chessBoard);

        return new ChessGame(chessBoard, Camp.WHITE);
    }

    private void play(ChessGame chessGame) {
        ChessCommand command;
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
        validateEqualPosition(fromInput, toInput);

        chessGame.move(toPosition(fromInput), toPosition(toInput));
    }

    private void validateEqualPosition(String fromInput, String toInput) {
        if (fromInput.equals(toInput)) {
            throw new IllegalArgumentException("출발 지점과 도착 지점은 동일할 수 없습니다");
        }
    }

    private Position toPosition(String positionInput) {
        String fileInput = String.valueOf(positionInput.charAt(FILE_INDEX));
        String rankInput = String.valueOf(positionInput.charAt(RANK_INDEX));

        return Position.of(ViewFile.from(fileInput), ViewRank.from(rankInput));
    }

    private void printBoard(Map<Position, Piece> piecesPosition) {
        outputView.printChessState(piecesPosition);
    }

    public void printErrorMessage(Exception exception) {
        outputView.printErrorMessage(exception.getMessage());
    }

    private <T> T retryOnInvalidUserInput(Supplier<T> request) {
        try {
            return request.get();
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            return retryOnInvalidUserInput(request);
        }
    }
}
