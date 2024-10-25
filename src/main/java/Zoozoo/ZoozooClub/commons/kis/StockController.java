package Zoozoo.ZoozooClub.commons.kis;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.account.service.AccountService;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

        Account account = accountService.getAccountById(accountId);
        BalanceResponseDTO balance = koreaInvestmentApiService.getStockBalance(account);
        return ResponseEntity.ok(balance);
    }
}