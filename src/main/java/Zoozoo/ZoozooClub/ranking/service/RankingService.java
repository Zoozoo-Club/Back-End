package Zoozoo.ZoozooClub.ranking.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.account.service.AccountService;
import Zoozoo.ZoozooClub.balance.service.BalanceService;
import Zoozoo.ZoozooClub.commons.kis.KoreaInvestmentApiService;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final AccountService accountService;
    private final KoreaInvestmentApiService koreaInvestmentApiService;
    private final BalanceService balanceService;

    /**
     * accountId 기준 단일 계좌 정보 업데이트
     * @param accountId pk
     */
    public void updateSingleAccountBalance(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        if (account != null) {
            BalanceResponseDTO balanceResponseDTO = koreaInvestmentApiService.getStockBalance(account);
            if (balanceResponseDTO != null &&
                    balanceResponseDTO.getOutput1() != null &&
                    balanceResponseDTO.getOutput2() != null) {
                balanceService.saveBalance(account.getId(), balanceResponseDTO);
            }
        }
    }

    /***
     * 1시간에 한 번씩 모든계좌정보 업데이트
     */
    @Scheduled(fixedRate = 3600000, initialDelay = 5000) // 1시간에 한 번 실행
    public void updateAllBalances() {

        List<Account> accounts = accountService.getAllAccounts();

        for (Account account : accounts) {
            BalanceResponseDTO balanceResponseDTO = koreaInvestmentApiService.getStockBalance(account);
            if (balanceResponseDTO != null && balanceResponseDTO.getOutput1() != null && balanceResponseDTO.getOutput2() != null) {
                balanceService.saveBalance(account.getId(), balanceResponseDTO);
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
                Map<String, Object> balances = balanceService.getBalancesByAccountId(accountIdLong);
                if (balances != null) {
                    System.out.println("Account ID: " + accountIdLong);
                    System.out.println("Output1: " + balances.get("output1"));
                    System.out.println("Output2: " + balances.get("output2"));
                }else{
                    System.out.println("balances are error.");
                }
            }

            List<Map<String, Object>> allBalances = balanceService.getAllBalances();
            System.out.println(allBalances);

        }

        System.out.println(analyzePortfolio(Long.parseLong("1")));

        System.out.println("All balances updated!");
    }

    /**
     *  총 평가금액 기준 상위 N개 계좌 조회
     * @param limit
     * @return
     */
    public List<Map<String, Object>> getTopNAccountsByTotalValue(int limit) {
        List<Map<String, Object>> allBalances = balanceService.getAllBalances();

        return allBalances.stream()
                .sorted((b1, b2) -> {
                    Map<String, Object> output2_1 = (Map<String, Object>) b1.get("output2");
                    Map<String, Object> output2_2 = (Map<String, Object>) b2.get("output2");

                    String amt1 = (String) output2_1.get("pchsAmtSmtlAmt");
                    String amt2 = (String) output2_2.get("pchsAmtSmtlAmt");

                    return Double.compare(
                            Double.parseDouble(amt2),
                            Double.parseDouble(amt1)
                    );
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 수익률 기준 상위 N개 계좌 조회
     * @param limit
     * @return
     */
    public List<Map<String, Object>> getTopNAccountsByProfitRate(int limit) {
        List<Map<String, Object>> allBalances = balanceService.getAllBalances();

        return allBalances.stream()
                .sorted((b1, b2) -> {
                    Map<String, Object> output2_1 = (Map<String, Object>) b1.get("output2");
                    Map<String, Object> output2_2 = (Map<String, Object>) b2.get("output2");

                    String profit1 = (String) output2_1.get("evluPflsSmtlAmt");
                    String profit2 = (String) output2_2.get("evluPflsSmtlAmt");

                    return Double.compare(
                            Double.parseDouble(profit2),
                            Double.parseDouble(profit1)
                    );
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     *  특정 종목 보유 계좌 조회
     */
    public List<Map<String, Object>> getAccountsByStockCode(String stockCode) {
        List<Map<String, Object>> allBalances = balanceService.getAllBalances();

        return allBalances.stream()
                .filter(balance -> {
                    List<Map<String, Object>> output1 = (List<Map<String, Object>>) balance.get("output1");
                    return output1.stream()
                            .anyMatch(stock -> stock.get("stockCode").equals(stockCode));
                })
                .collect(Collectors.toList());
    }

    /**
     * 계좌별 포트폴리오 분석
     * @param accountId
     * @return
     */
    public Map<String, Object> analyzePortfolio(Long accountId) {
        Map<String, Object> balance = balanceService.getBalancesByAccountId(accountId);
        if (balance == null) return null;

        List<Map<String, Object>> output1 = (List<Map<String, Object>>) balance.get("output1");
        Map<String, Object> output2 = (Map<String, Object>) balance.get("output2");

        // 분석 결과를 담을 Map
        Map<String, Object> analysis = new HashMap<>();

        // 총 보유 종목 수
        analysis.put("totalStocks", output1.size());

        // 최대 보유 종목
        analysis.put("largestHolding", output1.stream()
                .max((s1, s2) -> Double.compare(
                        Double.parseDouble((String) s1.get("evluAmt")),
                        Double.parseDouble((String) s2.get("evluAmt"))
                ))
                .orElse(null));

        // 전체 손익
        analysis.put("totalProfitLoss", output2.get("evluPflsSmtlAmt"));

        return analysis;
    }
}