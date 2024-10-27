package Zoozoo.ZoozooClub.commons.kis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class MyStockBalanceDto {
    private final String stockCode;
    private final String stockName;
    private final Integer quantity;
    private final Integer averagePrice;
    private final Integer currentPrice;
    private final Double earningRate;
}
