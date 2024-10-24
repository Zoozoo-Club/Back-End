package Zoozoo.ZoozooClub.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLettuceTest implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Redis에 데이터 저장
        redisTemplate.opsForValue().set("lettuceTestKey", "lettuceTestValue");

        // 저장된 데이터 Redis에서 조회
        Object value = redisTemplate.opsForValue().get("lettuceTestKey");

        // 결과 출력
        if ("lettuceTestValue".equals(value)) {
            System.out.println("Redis Lettuce 연결 성공: " + value);
        } else {
            System.out.println("Redis Lettuce 연결 실패 또는 잘못된 데이터: " + value);
        }
    }
}
