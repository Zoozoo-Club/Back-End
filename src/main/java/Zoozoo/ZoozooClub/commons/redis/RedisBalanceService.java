package Zoozoo.ZoozooClub.commons.redis;

import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RedisBalanceService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;

    @Autowired
    public RedisBalanceService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();  // ZSetOperations 초기화
    }
    public void saveBalance(Long accountId, BalanceResponseDTO balanceResponseDTO){
        String key = "account:balances";
        //TODO
        // - output1, output2 나눠서
        zSetOperations.add(key, balanceResponseDTO, accountId);

        redisTemplate.opsForSet().add("account:ids", accountId);

    }


    public Set<Object> getBalancesByAccountId(Object accountId) {
        String key = "account:balances:" + accountId;
        return zSetOperations.range(key, 0, -1);  // 해당 accountId에 대한 데이터만 조회

    }

    public List<Object> getAllBalances() {
        Set<Object> accountIds = redisTemplate.opsForSet().members("account:ids");  // 모든 accountId 조회
        List<Object> allBalances = new ArrayList<>();

        // 각 accountId별로 데이터를 가져와서 리스트에 추가
        for (Object accountId : accountIds) {
            Set<Object> balances = getBalancesByAccountId(accountId);
            allBalances.addAll(balances);  // 전체 리스트에 추가
        }

        return allBalances;
    }

}
