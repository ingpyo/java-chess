
package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.position.move.PieceMove;

import java.util.Objects;

public abstract class Piece {

    private final Camp camp;
    private final PieceSymbol pieceSymbol;

    Piece(final Camp camp, final PieceSymbol pieceSymbol) {
        this.camp = camp;
        this.pieceSymbol = pieceSymbol;
    }

    public boolean isBlack() {
        return camp == Camp.BLACK;
    }

    public boolean isEmpty() {
        return camp == Camp.NEUTRAL;
    }

    public boolean isKing() {
        return false;
    }

    public boolean isSameCamp(final Piece other) {
        if (other.camp == Camp.NEUTRAL) {
            return false;
        }

        return camp == other.camp;
    }

    public boolean isMyCamp(final Camp other) {
        return camp == other;
    }

    public abstract PieceMove getMovement(final Position from, Position to);

    abstract boolean isPieceRule(final Position from,final Position to);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return camp == piece.camp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(camp);
    }

    public PieceSymbol getPieceSymbol() {
        return pieceSymbol;
    }

    public Camp getCamp() {
        return camp;
    }
}