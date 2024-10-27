package Zoozoo.ZoozooClub.commons.kis;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.commons.auth.LoginUserId;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderRequestDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.OrderResponseDTO;
import Zoozoo.ZoozooClub.commons.kis.dto.StockPriceResponseDTO;
import Zoozoo.ZoozooClub.stock.entity.Stock;
import Zoozoo.ZoozooClub.stock.repository.StockRepository;
import Zoozoo.ZoozooClub.user.entity.User;
import Zoozoo.ZoozooClub.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService koreaInvestmentApiService;
    private final AuthService authService;
    private final StockRepository stockRepository;
    @GetMapping("/stocks/{stockCode}/current")
    public ResponseEntity<StockPriceResponseDTO> getCurrentPrice(@PathVariable String stockCode) {

        Stock stock = stockRepository.findByCode(stockCode);
        Long price = koreaInvestmentApiService.getCurrentPrice(stockCode);
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
}