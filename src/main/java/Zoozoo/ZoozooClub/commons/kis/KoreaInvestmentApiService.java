package Zoozoo.ZoozooClub.commons.kis;


import Zoozoo.ZoozooClub.commons.kis.dto.ApiResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class KoreaInvestmentApiService {

    private static final String BASE_URL = "https://openapivts.koreainvestment.com:29443";
    private static final String API_KEY = "PSXM8wuPZ3gNUosdS3MApfsLtd0OEuWZ69nD";
    private static final String API_SECRET = "1a4l1AYE08VluTNTiGZ9CqsGyDQvWZoyONeP53qQOV7kZ4sBPpr0BLd5IVhjr/qzlgrzmWd72ncgz3TGb7SBTtlKkXwLJOp8csrBxLHFey8Fa9EeIMIQjoPqwlSZBw0UYD6H9GAK7hn4UIqsQNJwZGzIhdzxJ8mh1eTKi2Sz6OWYTJ8vwio=";

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
                    .header("authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6IjBiYzVjNjc4LWVkNzctNDA1ZS05YzNhLTcxMjc2ZjQxNTY5ZiIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyOTgzMzY0MSwiaWF0IjoxNzI5NzQ3MjQxLCJqdGkiOiJQU1hNOHd1UFozZ05Vb3NkUzNNQXBmc0x0ZDBPRXVXWjY5bkQifQ.g3DqT7hjvNvz_ZT1A3eB151lZ2toe-1gt8WkJ2-lSf53TrxV28bbfLIr9UTzdn7wR1p-aA2wCKiLdBrDc1CPDQ")  // 실제 토큰으로 교체 필요
                    .header("appkey", API_KEY)
                    .header("appsecret", API_SECRET)
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

    public BalanceResponseDTO getStockBalance() {
        try {
            String url = String.format("%s/uapi/domestic-stock/v1/trading/inquire-balance" +
                            "?CANO=50112303" +           // 계좌번호
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
                    BASE_URL);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6IjBiYzVjNjc4LWVkNzctNDA1ZS05YzNhLTcxMjc2ZjQxNTY5ZiIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyOTgzMzY0MSwiaWF0IjoxNzI5NzQ3MjQxLCJqdGkiOiJQU1hNOHd1UFozZ05Vb3NkUzNNQXBmc0x0ZDBPRXVXWjY5bkQifQ.g3DqT7hjvNvz_ZT1A3eB151lZ2toe-1gt8WkJ2-lSf53TrxV28bbfLIr9UTzdn7wR1p-aA2wCKiLdBrDc1CPDQ")  // 인증 토큰
                    .header("appkey", API_KEY)                     // 앱 키
                    .header("appsecret", API_SECRET)              // 앱 시크릿
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

}