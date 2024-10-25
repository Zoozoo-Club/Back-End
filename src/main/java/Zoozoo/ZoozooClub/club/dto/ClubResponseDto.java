package Zoozoo.ZoozooClub.club.dto;


import Zoozoo.ZoozooClub.company.entity.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

        @RequiredArgsConstructor
        @Getter
        static class StockHoldings {
            private final String stockCode;
            private final String stockName;
            private final Double holdingRatio;
            private final Double roi;
            private final Integer holdingMembers;
        }
    }
}
