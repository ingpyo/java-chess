package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.move.BlockingMove;
import chess.domain.position.move.InvalidMove;
import chess.domain.position.move.PieceMove;

public final class Bishop extends Piece {

    public Bishop(Camp camp) {
        super(camp, PieceSymbol.BISHOP);
    }

    @Override
    public PieceMove getMovement(Position from, Position to) {
        if (isPeaceRule(from, to)) {
            return new BlockingMove();
        }

        return new InvalidMove();
    }

    @Override
    boolean isPeaceRule(Position from, Position to) {
        int fileGap = from.calculateFileGap(to);
        int rankGap = from.calculateRankGap(to);

        return Math.abs(fileGap) == Math.abs(rankGap);
    }

}
