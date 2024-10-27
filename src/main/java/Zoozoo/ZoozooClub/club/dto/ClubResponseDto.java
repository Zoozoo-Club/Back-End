package Zoozoo.ZoozooClub.club.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class ClubResponseDto {

    private final CompanyInfo companyInfo;
    private final ClubPortfolio clubPortfolio;

    @Getter
    @Builder
    public static class CompanyInfo {
        private final Long companyId;
        private final String companyName;
        private final String logoId;
        private final String websiteUrl;
    }

    @Getter
    @Builder
    public static class ClubPortfolio {
        private final int totalMembers;
        private final long totalInvestmentAmount;
        private final List<StockHoldings> stockHoldings;

        @Getter
        @Builder
        public static class StockHoldings {
            private String stockCode;
            private String stockName;
            // 갖고 있는 비율
            @Setter
            private Double holdingRatio;
            // 수익률
            @Setter
            private Double roi;
            @Setter
            private Integer holdingMembers;
        }
    }
}
