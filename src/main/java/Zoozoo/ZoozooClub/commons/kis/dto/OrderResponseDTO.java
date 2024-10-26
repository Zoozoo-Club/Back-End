package Zoozoo.ZoozooClub.commons.kis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseDTO {
    @JsonProperty("rt_cd")
    private String rtCd;          // 응답코드 (0: 성공, 그 외: 실패)

    @JsonProperty("msg_cd")
    private String msgCd;         // 응답메시지코드

    @JsonProperty("msg1")
    private String msg;           // 응답메시지



//    private Output output;        // 응답 상세 데이터

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Output {


//        @JsonProperty("KRX_FWDG_ORD_ORGNO")
//        private String orderOrgNo;      // 주문기관번호

    }
}