package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    @DisplayName("포인트간의 랭크의 차이를 계산한다.")
    void calculateRankGap() {
        Position from = new Position(File.A, Rank.ONE);
        Position to = new Position(File.C, Rank.FOUR);

        int rankGap = from.calculateRankGap(to);

        assertThat(rankGap).isEqualTo(-3);
    }

    @Test
    @DisplayName("포인트간의 파일의 차이를 계산한다.")
    void calculateFileGap() {
        Position from = new Position(File.A, Rank.ONE);
        Position to = new Position(File.C, Rank.FOUR);

        int fileGap = from.calculateFileGap(to);

        assertThat(fileGap).isEqualTo(-2);
    }

    @Nested
    @DisplayName("시작 포인트와 도착 포인트 사이의 포인트들을 구할 수 있다.")
    class GetBetweenPositionsTest {
        @Test
        @DisplayName("A1 -> C3")
        void A1ToC3PointsTest() {
            Position from = new Position(File.A, Rank.ONE);
            Position to = new Position(File.C, Rank.THREE);

            List<Position> betweenPositions = from.getBetweenPositions(to);

            assertThat(betweenPositions).contains(new Position(File.B, Rank.TWO));
        }

        @Test
        @DisplayName("C1 -> A3")
        void C1ToA3PointsTest() {
            Position from = new Position(File.C, Rank.ONE);
            Position to = new Position(File.A, Rank.THREE);

            List<Position> betweenPositions = from.getBetweenPositions(to);

            assertThat(betweenPositions).contains(new Position(File.B, Rank.TWO));
        }

        @Test
        @DisplayName("D4 -> A1")
        void D4ToA1PointsTest() {
            Position from = new Position(File.D, Rank.FOUR);
            Position to = new Position(File.A, Rank.ONE);

            List<Position> betweenPositions = from.getBetweenPositions(to);

            assertThat(betweenPositions).contains(new Position(File.B, Rank.TWO), new Position(File.C, Rank.THREE));
        }

        @Test
        @DisplayName("A1 -> A4")
        void A1ToA4PointsTest() {
            Position from = new Position(File.A, Rank.ONE);
            Position to = new Position(File.A, Rank.FOUR);

            List<Position> betweenPositions = from.getBetweenPositions(to);

            assertThat(betweenPositions).contains(new Position(File.A, Rank.TWO), new Position(File.A, Rank.THREE));
        }

        @Test
        @DisplayName("A1 -> D1")
        void A1ToD1PointsTest() {
            Position from = new Position(File.A, Rank.ONE);
            Position to = new Position(File.D, Rank.ONE);

            List<Position> betweenPositions = from.getBetweenPositions(to);

            assertThat(betweenPositions).contains(new Position(File.B, Rank.ONE), new Position(File.C, Rank.ONE));
        }
    }

}
