package Zoozoo.ZoozooClub.commons.kis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponseDTO {

    private List<BalanceOutput> output1;  // 응답 데이터 리스트

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class BalanceOutput {
        @JsonProperty("pdno")
        private String stockCode;        // 종목코드

        @JsonProperty("prdt_name")
        private String stockName;        // 종목명

        @JsonProperty("hldg_qty")
        private String quantity;         // 보유수량

        @JsonProperty("pchs_avg_pric")
        private String avgPrice;         // 매입평균가격

        @JsonProperty("prpr")
        private String currentPrice;     // 현재가

        @JsonProperty("evlu_pfls_amt")
        private String profitLoss;       // 평가손익금액

        @JsonProperty("evlu_erng_rt")
        private String profitLossRate;   // 수익률

    }
}