package Zoozoo.ZoozooClub.commons.kis;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderRequestDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService koreaInvestmentApiService;
    private final AuthService authService;

    @GetMapping("/clubs/{stockCode}/current")
    public ResponseEntity<StockPriceResponseDTO> getCurrentPrice(@PathVariable String stockCode) {
        Long price = koreaInvestmentApiService.getCurrentPrice(stockCode);
        return ResponseEntity.ok(new StockPriceResponseDTO(stockCode, price));
    }

    @GetMapping("/my-story/assets")
    public ResponseEntity<BalanceResponseDTO> getStockBalance(@LoginUserId Long userId) {
        User user = authService.getUserById(userId);
        Account account = user.getAccount();
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