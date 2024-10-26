package Zoozoo.ZoozooClub.ranking.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InClubRanking {
    private final Long userId;
    private final String userNickname;
    private final Double profit;
}
