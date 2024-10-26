package Zoozoo.ZoozooClub.ranking.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingDto {
    private final Long clubId;
    private final String clubName;
    private Double roi;
    private Long profitValue;
    private Long totalAmount;
    private Long userCount;
}
