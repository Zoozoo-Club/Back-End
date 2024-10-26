package Zoozoo.ZoozooClub.commons.kis;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.account.service.AccountService;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderRequestDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService koreaInvestmentApiService;
    private final AccountService accountService;

    @GetMapping("/clubs/{stockCode}/current")
    public ResponseEntity<StockPriceResponseDTO> getCurrentPrice(@PathVariable String stockCode) {
        Long price = koreaInvestmentApiService.getCurrentPrice(stockCode);
        return ResponseEntity.ok(new StockPriceResponseDTO(stockCode, price));
    }

    @GetMapping("/my-story/assets/{accountId}")
    public ResponseEntity<BalanceResponseDTO> getStockBalance(@PathVariable Long accountId) {

        Account account = accountService.getAccount(accountId);
        BalanceResponseDTO balance = koreaInvestmentApiService.getStockBalance(account);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/my-story/order/{accountId}")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @PathVariable Long accountId,
            @RequestBody OrderRequestDTO orderRequest) {

        Account account = accountService.getAccount(accountId);
        OrderResponseDTO response = koreaInvestmentApiService.placeOrder(account, orderRequest);
        return ResponseEntity.ok(response);
    }
}