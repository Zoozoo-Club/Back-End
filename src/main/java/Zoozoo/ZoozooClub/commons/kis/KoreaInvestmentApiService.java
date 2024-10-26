package Zoozoo.ZoozooClub.commons.kis;


import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.commons.kis.dto.ApiResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderRequestDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KoreaInvestmentApiService {

    private static final String BASE_URL = "https://openapivts.koreainvestment.com:29443";

    @Value("${korea-investment.api.key}")
    private String apiKey;

    @Value("${korea-investment.api.secret}")
    private String apiSecret;

    @Value("${korea-investment.api.account-number}")
    private String accountNumber;

    @Value("${korea-investment.api.token}")
    private String token;


    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Long getCurrentPrice(String stockCode) {
        try {
            String url = String.format("%s/uapi/domestic-stock/v1/quotations/inquire-time-itemconclusion" +
                            "?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=%s&FID_INPUT_HOUR_1=160000",
                    BASE_URL, stockCode);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("authorization", "Bearer " + token)  // 실제 토큰으로 교체 필요
                    .header("appkey", apiKey)
                    .header("appsecret", apiSecret)
                    .header("tr_id", "FHPST01060000")
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ApiResponseDTO apiResponse = objectMapper.readValue(response.body(),
                    ApiResponseDTO.class);

            if (apiResponse != null && apiResponse.getOutput1() != null) {
                return Long.parseLong(apiResponse.getOutput1().getStck_prpr());
            }

            return 0L;
        } catch (Exception e) {
            log.error("Error getting stock price for code {}: {}", stockCode, e.getMessage());
            return 0L;
        }
    }

    public BalanceResponseDTO getStockBalance(Account account) {
        try {
            String url = String.format("%s/uapi/domestic-stock/v1/trading/inquire-balance" +
                            "?CANO=%s" +           // 계좌번호
                            "&ACNT_PRDT_CD=01" +         // 계좌상품코드
                            "&AFHR_FLPR_YN=N" +          // 시간외단일가여부
                            "&OFL_YN=" +                 // 오프라인여부
                            "&INQR_DVSN=02" +           // 조회구분
                            "&UNPR_DVSN=01" +           // 단가구분
                            "&FUND_STTL_ICLD_YN=N" +     // 펀드결제분포함여부
                            "&FNCG_AMT_AUTO_RDPT_YN=N" + // 융자금액자동상환여부
                            "&PRCS_DVSN=01" +           // 처리구분
                            "&CTX_AREA_FK100=" +         // 연속조회검색조건
                            "&CTX_AREA_NK100=",          // 연속조회검색조건
                    BASE_URL,account.getAcctNo());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("authorization", "Bearer " + account.getAccessToken())  // 인증 토큰
                    .header("appkey", account.getAppKey())                     // 앱 키
                    .header("appsecret", account.getSecretKey())              // 앱 시크릿
                    .header("tr_id", "VTTC8434R")                 // 거래ID
                    .header("Content-Type", "application/json")    // 컨텐츠 타입
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), BalanceResponseDTO.class);
        } catch (Exception e) {
            log.error("Error getting stock balance: {}", e.getMessage());
            return null;
        }
    }

    public OrderResponseDTO placeOrder(Account account, OrderRequestDTO orderRequest) {
        try {
            String url = String.format("%s/uapi/domestic-stock/v1/trading/order-cash", BASE_URL);

            //시장가 주문(01)으로 고정, 수량 1주로 고정
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "CANO", account.getAcctNo(),           // 계좌번호
                    "ACNT_PRDT_CD", "01",                  // 계좌상품코드
                    "PDNO", orderRequest.getStockCode(),   // 종목코드
                    "ORD_DVSN", "01",                      // 주문유형 (시장가 주문)
                    "ORD_QTY", "1",                        // 주문수량 (1주 고정)
                    "ORD_UNPR", "0",                       // 주문단가 (시장가이므로 0)
                    "CTAC_TLNO", "",                       // 연락전화번호
                    "SLL_BUY_DVSN", orderRequest.getOrderSide(), // 매매구분
                    "ALGO_NO", ""                          // 알고리즘ID
            ));

            // TR_ID 설정 (매수/매도에 따라 다름)
            String trId = orderRequest.getOrderSide().equals("01") ? "VTTC0802U" : "VTTC0801U";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("authorization", "Bearer " + account.getAccessToken())
                    .header("appkey", account.getAppKey())
                    .header("appsecret", account.getSecretKey())
                    .header("tr_id", trId)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), OrderResponseDTO.class);
        } catch (Exception e) {
            log.error("Error placing order: {}", e.getMessage());
            OrderResponseDTO errorResponse = new OrderResponseDTO();
            errorResponse.setRtCd("-1");
            errorResponse.setMsg("주문 처리 중 오류가 발생했습니다: " + e.getMessage());
            return errorResponse;
        }
    }

}