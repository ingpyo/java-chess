package chess.view;

import chess.domain.position.Rank;

import java.util.Arrays;

public enum ViewRank {
    EIGHT(Rank.EIGHT, "8"),
    SEVEN(Rank.SEVEN, "7"),
    SIX(Rank.SIX, "6"),
    FIVE(Rank.FIVE, "5"),
    FOUR(Rank.FOUR, "4"),
    THREE(Rank.THREE, "3"),
    TWO(Rank.TWO, "2"),
    ONE(Rank.ONE, "1"),
    ;

    private final Rank rank;
    private final String viewRank;

    ViewRank(final Rank rank, final String viewRank) {
        this.rank = rank;
        this.viewRank = viewRank;
    }

    public static Rank from(final String viewRank) {
        return Arrays.stream(ViewRank.values())
                .filter(it -> it.viewRank.equals(viewRank))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랭크 입니다."))
                .rank;
    }
}
