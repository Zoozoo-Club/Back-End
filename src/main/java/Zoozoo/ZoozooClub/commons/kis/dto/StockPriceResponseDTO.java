package Zoozoo.ZoozooClub.commons.kis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceResponseDTO {
    private String stockCode;
    private Long currentPrice;
}
