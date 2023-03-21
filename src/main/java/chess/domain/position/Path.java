package chess.domain.position;

import chess.domain.piece.Piece;
import chess.domain.position.move.PieceMove;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private static final String UNABLE_TO_MOVE_ERROR_MESSAGE = "이동할 수 없습니다.";

    public List<Position> getBetweenPositions(Position fromPosition, Position toPosition) {
        int fileGap = fromPosition.calculateFileGap(toPosition);
        int rankGap = fromPosition.calculateRankGap(toPosition);

        return getBetweenPositions(toPosition, fileGap, rankGap);
    }


    private List<Position> getBetweenPositions(Position other, int fileGap, int rankGap) {
        int distance = Math.max(Math.abs(fileGap), Math.abs(rankGap));
        int fileUnitGap = fileGap / distance;
        int rankUnitGap = rankGap / distance;

        List<Position> between = new ArrayList<>();
        for (int i = 1; i < distance; i++) {
            other = other.move(fileUnitGap, rankUnitGap);
            between.add(other);
        }
        return between;
    }

    public void judgeBetweenStuck(List<Piece> betweenPieces, PieceMove pieceMove) {
        for (Piece piece : betweenPieces) {
            validateMovable(piece, pieceMove, false);
        }
    }

    public void validateMovable(Piece piece, PieceMove pieceMove,boolean lastPiece) {
        if (!pieceMove.isMovable(piece, lastPiece)) {
            throw new IllegalArgumentException(UNABLE_TO_MOVE_ERROR_MESSAGE);
        }
    }
}
