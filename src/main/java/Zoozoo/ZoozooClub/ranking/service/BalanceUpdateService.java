package Zoozoo.ZoozooClub.ranking.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.account.service.AccountService;
import Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.commons.redis.RedisBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceUpdateService {
    private final AccountService accountService;
    private final KoreaInvestmentApiService koreaInvestmentApiService;
    private final RedisBalanceService redisBalanceService;

    @Scheduled(fixedRate = 3600000, initialDelay = 5000) // 1시간에 한 번 실행
    public void updateAllBalances() {
        // 잔고 목록 업데이트 로직
        //TODO
        // - 모든 계좌 정보 업데이트
        List<Account> accounts = accountService.getAllAccounts();

        //TODO
        // - 각 계좌의 잔고 업데이트 로직 작성
        for (Account account : accounts) {
            //TODO
            // getStockBalance에 넣어주기
            BalanceResponseDTO balanceResponseDTO = koreaInvestmentApiService.getStockBalance(account);
            if (balanceResponseDTO != null && balanceResponseDTO.getOutput1() != null && balanceResponseDTO.getOutput2() != null) {
                redisBalanceService.saveBalance(account.getId(), balanceResponseDTO);
            } else {
                System.out.println("No balance data available for account: " + account.getId());
            }


        }



        System.out.println("All balances updated!");

    }
}