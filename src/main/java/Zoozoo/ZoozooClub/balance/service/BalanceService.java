package Zoozoo.ZoozooClub.balance.service;

import Zoozoo.ZoozooClub.account.entity.Account;
import Zoozoo.ZoozooClub.balance.exception.NotFoundBalanceException;
import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import Zoozoo.ZoozooClub.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthService authService;
    @Autowired
    public BalanceService(RedisTemplate<String, Object> redisTemplate, AuthService authService) {
        this.redisTemplate = redisTemplate;
        this.authService = authService;
    }

    /**
     * accountId기준 잔고 저장
     * @param accountId
     * @param balanceResponseDTO
     */
    public void saveBalance(Long accountId, BalanceResponseDTO balanceResponseDTO) {
        // 계좌별 잔고 데이터 저장
        String key = "balance:accounts:" + accountId;
        Map<String, Object> balanceData = new HashMap<>();

        // output1 데이터 처리
        List<Map<String, Object>> output1List = balanceResponseDTO.getOutput1().stream()
                .map(BalanceResponseDTO.BalanceOutput1::getMap)
                .collect(Collectors.toList());

        // output2 데이터 처리
        Map<String, Object> output2Data = balanceResponseDTO.getOutput2().isEmpty()
                ? new HashMap<>()
                : balanceResponseDTO.getOutput2().get(0).getMap();

        // 전체 데이터 구성
        balanceData.put("output1", output1List);
        balanceData.put("output2", output2Data);

        // Redis에 저장
        redisTemplate.opsForValue().set(key, balanceData);

        // 계좌 ID 목록 저장
        redisTemplate.opsForSet().add("balance:account:ids", accountId);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBalancesByAccountId(Long accountId) {
        String key = "balance:accounts:" + accountId;
        return (Map<String, Object>) redisTemplate.opsForValue().get(key);
    }

    /**
     * 모든 계좌의 잔고 조회
     * @return
     */
    public List<Map<String, Object>> getAllBalances() {
        // 계좌 ID 목록 조회 키를 saveBalance와 일치시킴
        Set<Object> accountIds = redisTemplate.opsForSet().members("balance:account:ids");

        return accountIds.stream()
                .map(this::convertToLong)
                .filter(Objects::nonNull)
                .map(this::getBalancesByAccountId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    private Long convertToLong(Object id) {
        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        } else if (id instanceof Long) {
            return (Long) id;
        } else if (id instanceof String) {
            return Long.parseLong((String) id);
        }
        return null;
    }


    /***
     * accountId 기준 계좌 삭제
     * @param accountId
     */
    public void deleteBalance(Long accountId) {
        String key = "balance:accounts:" + accountId;
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove("balance:account:ids", accountId);
    }

    /**
     * userId 기준 수익률 조회
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public double getProfitByUserId(Long userId) {
        Account account = authService.getAccountIdById(userId);
        String key = "balance:accounts:" + account.getId();

        Map<String, Object> balanceData = (Map<String, Object>) redisTemplate.opsForValue().get(key);
        if (balanceData == null) {
            throw new NotFoundBalanceException();
        }

        Map<String, Object> output2 = (Map<String, Object>) balanceData.get("output2");
        if (output2 == null || !output2.containsKey("evluPflsSmtlAmt") || !output2.containsKey("pchsAmtSmtlAmt")) {
            throw new NotFoundBalanceException();
        }

        // 평가손익금액과 매입금액 추출
        double evaluationProfit = convertToDouble(output2.get("evluPflsSmtlAmt")); // 평가손익금액
        double purchaseAmount = convertToDouble(output2.get("pchsAmtSmtlAmt")); // 매입금액

        if (purchaseAmount == 0) {
            return 0.0;
        }

        double profitRate = (evaluationProfit / purchaseAmount) * 100;
        return Math.round(profitRate * 100.0) / 100.0;
    }

    /**
     * Long으로 변환
     * @param value
     * @return
     */
    private double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        throw new IllegalStateException("잘못된 데이터 형식입니다.");
    }
}