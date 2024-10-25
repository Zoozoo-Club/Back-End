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

    private List<BalanceOutput1> output1;
    private List<BalanceOutput2> output2;


    // 응답 데이터 리스트
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class BalanceOutput1 {
        /**
         * 종목코드
         */
        @JsonProperty("pdno")
        private String stockCode;

        /**
         * 종목명
         */
        @JsonProperty("prdt_name")
        private String stockName;

        /**
         * 보유수량
         */
        @JsonProperty("hldg_qty")
        private String quantity;

        /**
         * 평가금액
         */
        @JsonProperty("evlu_amt")
        private String evluAmt;

        /**
         * 평가손익금액
         */
        @JsonProperty("evlu_pfls_amt")
        private String evluPflsAmt;

        /**
         * 평가손익률
         */
        @JsonProperty("evlu_pfls_rt")
        private String evluPflsRt;

        /**
         * 매입평균가격
         */
        @JsonProperty("pchs_avg_pric")
        private String pchsAvgPric;

        /**
         * 현재가
         */
        @JsonProperty("prpr")
        private String currentPrice;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class BalanceOutput2 {
        /**
         * 평가손익합계금액
         */
        @JsonProperty("evlu_pfls_smtl_amt")
        private String evluPflsSmtlAmt;

        /**
         * 매입금액합계금액
         */
        @JsonProperty("pchs_amt_smtl_amt")
        private String pchsAmtSmtlAmt;
    }
}