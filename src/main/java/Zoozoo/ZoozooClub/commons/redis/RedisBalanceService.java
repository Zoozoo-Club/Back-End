package Zoozoo.ZoozooClub.commons.redis;
//
//import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ZSetOperations;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//@Service
//@AllArgsConstructor
//public class RedisBalanceService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private ZSetOperations<String, Object> zSetOperations;
//
//    @Autowired
//    public RedisBalanceService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.zSetOperations = redisTemplate.opsForZSet();  // ZSetOperations 초기화
//    }
//    public void saveBalance(Long accountId, BalanceResponseDTO balanceResponseDTO){
//        String key = "account:balances:"+accountId;
//        //TODO
//        // - output1, output2 나눠서
//        zSetOperations.add(key, balanceResponseDTO, accountId);
//        redisTemplate.opsForSet().add("account:ids", accountId);
//
//    }
//
//
//    public Set<Object> getBalancesByAccountId(Object accountId) {
//        String key = "account:balances:" + accountId;
//        return zSetOperations.range(key, 0, -1);  // 해당 accountId에 대한 데이터만 조회
//
//    }
//
//    public List<Object> getAllBalances() {
//        Set<Object> accountIds = redisTemplate.opsForSet().members("account:ids");  // 모든 accountId 조회
//        List<Object> allBalances = new ArrayList<>();
//
//        // 각 accountId별로 데이터를 가져와서 리스트에 추가
//        for (Object accountId : accountIds) {
//            Set<Object> balances = getBalancesByAccountId(accountId);
//            allBalances.addAll(balances);  // 전체 리스트에 추가
//        }
//
//        return allBalances;
//    }
//
//}
//
//import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class RedisBalanceService {
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @Autowired
//    public RedisBalanceService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public void saveBalance(Long accountId, BalanceResponseDTO balanceResponseDTO) {
//        String key = "balance:accounts:" + accountId;
//
//        Map<String, Object> balanceData = new HashMap<>();
//
//        // output1 데이터 처리 - ArrayList로 직접 변환
//        List<Map<String, Object>> output1List = balanceResponseDTO.getOutput1().stream()
//                .map(BalanceResponseDTO.BalanceOutput1::getMap)
//                .collect(Collectors.toList());
//
//        // output2 데이터 처리
//        Map<String, Object> output2Data = balanceResponseDTO.getOutput2().isEmpty()
//                ? new HashMap<>()
//                : balanceResponseDTO.getOutput2().get(0).getMap();
//
//        // 전체 데이터 구성
//        balanceData.put("output1", output1List);
//        balanceData.put("output2", output2Data);
//
//        // Redis에 저장
//        redisTemplate.opsForValue().set(key, balanceData);
//    }
//
//    @SuppressWarnings("unchecked")
//    public Map<String, Object> getBalancesByAccountId(Long accountId) {
//        String key = "balance:accounts:" + accountId;
//        return (Map<String, Object>) redisTemplate.opsForValue().get(key);
//    }
//
//    public List<Map<String, Object>> getAllBalances() {
//        Set<Object> accountIds = redisTemplate.opsForSet().members("account:ids");
//        return accountIds.stream()
//                .map(id -> getBalancesByAccountId((Long) id))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//}



import Zoozoo.ZoozooClub.commons.kis.dto.BalanceResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisBalanceService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisBalanceService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
     * 등록된 accountId
     * @return
     */
    public Set<Object> getAllAccountIds() {
        return redisTemplate.opsForSet().members("balance:account:ids");
    }
}