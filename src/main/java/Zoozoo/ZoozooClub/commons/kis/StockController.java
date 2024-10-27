package Zoozoo.ZoozooClub.commons.kis;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.balance.service.BalanceService;
import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderRequestDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.stock.repository.StockRepository;
import Zoozoo.ZoozooClub.commons.kis.dto.*;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final KoreaInvestmentApiService koreaInvestmentApiService;
    private final AuthService authService;
    private final BalanceService balanceService;
    private final StockRepository stockRepository;
    @GetMapping("/stocks/{stockCode}/current")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "assets API")
    public ResponseEntity<StockPriceResponseDTO> getCurrentPrice(@PathVariable String stockCode) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        Long price = koreaInvestmentApiService.getCurrentPrice(stockCode,account);
        Stock stock = stockRepository.findByCode(stockCode);
        return ResponseEntity.ok(new StockPriceResponseDTO(stockCode, stock.getName(), price));
    }

    @GetMapping("/my-story/assets")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "test API")
    public ResponseEntity<BalanceResponseDTO> getStockBalance(@LoginUserId Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        BalanceResponseDTO balance = koreaInvestmentApiService.getStockBalance(account);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/my-story/assets/{userId}")
    public ResponseEntity<OtherStockBalanceDto> getOtherStockBalance(@PathVariable("userId") Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        OtherStockBalanceDto otherStockBalanceDto = null;
        BalanceResponseDTO balance = koreaInvestmentApiService.getStockBalance(account);

        for (BalanceResponseDTO.BalanceOutput2 balanceOutput2 : balance.getOutput2()) {
            long profit = Long.parseLong(balanceOutput2.getPchsAmtSmtlAmt());

            if(profit < 100_000_000) {
                otherStockBalanceDto = new OtherStockBalanceDto("1억원 미만");
            } else if(profit < 300_000_000) {
                otherStockBalanceDto = new OtherStockBalanceDto("1억원 이상 3억원 미만");
            } else if(profit < 500_000_000) {
                otherStockBalanceDto = new OtherStockBalanceDto("3억원 이상 5억원 미만");
            } else if(profit < 1_000_000_000) {
                otherStockBalanceDto = new OtherStockBalanceDto("5억원 이상 10억원 미만");
            } else if(profit < 5_000_000_000L) {
                otherStockBalanceDto = new OtherStockBalanceDto("10억원 이상 50억원 미만");
            } else if(profit < 10_000_000_000L) {
                otherStockBalanceDto = new OtherStockBalanceDto("50억원 이상 100억원 미만");
            } else {
                otherStockBalanceDto = new OtherStockBalanceDto("100억원 이상");
            }

        }

        return ResponseEntity.ok(otherStockBalanceDto);
    }

    @PostMapping("/my-story/order")
    @SecurityRequirement(name="JWT")
    @Operation(summary = "test API")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @LoginUserId Long userId,
            @RequestBody OrderRequestDTO orderRequest) {

        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        OrderResponseDTO response = koreaInvestmentApiService.placeOrder(account, orderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-story/holdings")
    public ResponseEntity<List<MyStockBalanceDto>> getMyStock(@LoginUserId Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        Map<String, Object> map = balanceService.getBalancesByAccountId(account.getId());

        List<MyStockBalanceDto> myStockBalanceDtos = new ArrayList<>();
        List<Map<String, Object>> lists = (List<Map<String, Object>>) map.get("output1");

        for(Map<String, Object> balanceOutput1 : lists) {
            myStockBalanceDtos.add(MyStockBalanceDto.builder()
                            .stockName((String) balanceOutput1.get("stockName"))
                            .stockCode((String) balanceOutput1.get("stockCode"))
                            .quantity(Integer.valueOf((String) balanceOutput1.get("quantity")))
                            .averagePrice(Integer.valueOf((String) balanceOutput1.get("evluAmt")))
                            .earningRate(Double.parseDouble((String) balanceOutput1.get("evluPflsRt")))
                            .currentPrice(Integer.valueOf((String) balanceOutput1.get("currentPrice")))
                    .build());
        }

        return ResponseEntity.ok(myStockBalanceDtos);

    }

    @GetMapping("/my-story/holdings/{userId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "타인의 주식 종목 조회 API")
    public ResponseEntity<List<MyStockBalanceDto>> getOtherStock(@PathVariable("userId") Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
        Map<String, Object> map = balanceService.getBalancesByAccountId(account.getId());

        List<MyStockBalanceDto> myStockBalanceDtos = new ArrayList<>();
        List<Map<String, Object>> lists = (List<Map<String, Object>>) map.get("output1");

        for(Map<String, Object> balanceOutput1 : lists) {
            myStockBalanceDtos.add(MyStockBalanceDto.builder()
                    .stockName((String) balanceOutput1.get("stockName"))
                    .stockCode((String) balanceOutput1.get("stockCode"))
                    .quantity(Integer.valueOf((String) balanceOutput1.get("quantity")))
                    .averagePrice(Integer.valueOf((String) balanceOutput1.get("evluAmt")))
                    .earningRate(Double.parseDouble((String) balanceOutput1.get("evluPflsRt")))
                    .currentPrice(Integer.valueOf((String) balanceOutput1.get("currentPrice")))
                    .build());
        }

        return ResponseEntity.ok(myStockBalanceDtos);

    }
}