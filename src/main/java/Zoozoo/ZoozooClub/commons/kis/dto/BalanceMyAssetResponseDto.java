package Zoozoo.ZoozooClub.commons.kis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class BalanceMyAssetResponseDto {
    private List<MyAssetOutput1> output1;
    private List<MyAssetOutput2> output2;

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class MyAssetOutput1 implements Serializable {
        private static final long serialVersionUID = 1L;
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

        @JsonProperty("holdingRatio")
        private String holdingRatio;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('[').append(stockCode).append(']').append('[').append(stockName).append("]: ").append(quantity)
                    .append(evluAmt).append('[').append(evluPflsAmt).append("]: ").append(evluPflsRt);
            return stringBuilder.toString();
        }

        public Map<String, Object> getMap() {
            return Map.ofEntries(
                    Map.entry("stockCode", this.stockCode),
                    Map.entry("stockName", this.stockName),
                    Map.entry("quantity", this.quantity),
                    Map.entry("evluAmt", this.evluAmt),
                    Map.entry("evluPflsAmt", this.evluPflsAmt),
                    Map.entry("evluPflsRt", this.evluPflsRt),
                    Map.entry("pchsAvgPric", this.pchsAvgPric),
                    Map.entry("currentPrice", this.currentPrice)
            );
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class BalanceOutput2 implements Serializable {
        private static final long serialVersionUID = 1L;
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

        public Map<String, Object> getMap() {
            return Map.ofEntries(
                    Map.entry( "evluPflsSmtlAmt", this.evluPflsSmtlAmt ),
                    Map.entry( "pchsAmtSmtlAmt", this.pchsAmtSmtlAmt )
            );
        }
    }
    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)  // 추가
    public static class MyAssetOutput2 implements Serializable {
        private static final long serialVersionUID = 1L;
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

        @JsonProperty("roi")
        private Double roi;

        public Map<String, Object> getMap() {
            return Map.ofEntries(
                    Map.entry( "evluPflsSmtlAmt", this.evluPflsSmtlAmt ),
                    Map.entry( "pchsAmtSmtlAmt", this.pchsAmtSmtlAmt )
            );
        }
    }
}

