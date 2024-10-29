package Zoozoo.ZoozooClub.commons.kis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
public class StockBalanceResponseDto {

    private final List<MyStockBalanceDto> stocksInfos;
    private final Double roi;


    @Getter
    @Builder
    public static class MyStockBalanceDto implements Comparable<MyStockBalanceDto> {
        private final String stockCode;
        private final String stockName;
        private final Integer quantity;
        private final Integer averagePrice;
        private final Integer currentPrice;
        private final Double earningRate;
        private final Double holdingRatio;

        @Override
        public int compareTo(MyStockBalanceDto o) {
            if(o.holdingRatio < this.holdingRatio) return -1;
            else if(o.holdingRatio > this.holdingRatio) return 1;
            return 0;
        }
    }
}
