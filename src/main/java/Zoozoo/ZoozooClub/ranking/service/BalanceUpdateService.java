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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BalanceUpdateService {
    private final AccountService accountService;
    private final KoreaInvestmentApiService koreaInvestmentApiService;
    private final RedisBalanceService redisBalanceService;

    @Scheduled(fixedRate = 3600000, initialDelay = 5000) // 1시간에 한 번 실행
    public void updateAllBalances() {

        List<Account> accounts = accountService.getAllAccounts();

        for (Account account : accounts) {
            BalanceResponseDTO balanceResponseDTO = koreaInvestmentApiService.getStockBalance(account);
            if (balanceResponseDTO != null && balanceResponseDTO.getOutput1() != null && balanceResponseDTO.getOutput2() != null) {
                redisBalanceService.saveBalance(account.getId(), balanceResponseDTO);
            } else {
                System.out.println("No balance data available for account: " + account.getId());
            }
        }

        for (Account account : accounts) {
            // Object를 Long으로 변환
            Long accountIdLong = null;
            if (account.getId() instanceof Long) {
                accountIdLong = (Long) account.getId() ;
            }

            if (accountIdLong != null) {
                Map<String, Object> balances = redisBalanceService.getBalancesByAccountId(accountIdLong);
                if (balances != null) {
                    System.out.println("Account ID: " + accountIdLong);
                    System.out.println("Output1: " + balances.get("output1"));
                    System.out.println("Output2: " + balances.get("output2"));
                }else{
                    System.out.println("DDD");
                }
            }

            if(accountIdLong % 2 == 0){
                redisBalanceService.deleteBalance(accountIdLong);
            }

            List<Map<String, Object>>  allBalances = redisBalanceService.getAllBalances();
            System.out.println(allBalances);

        }


        System.out.println("All balances updated!");
        Map<String, Object> balance = redisBalanceService.getBalancesByAccountId(Long.parseLong("1"));
        System.out.println(balance);
    }


}