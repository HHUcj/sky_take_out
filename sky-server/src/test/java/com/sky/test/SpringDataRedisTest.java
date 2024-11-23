//package com.sky.test;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.core.ZSetOperations;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * ClassName: SpringDataRedisTest
// * Description:
// *
// * @Author: 陈杰
// * @Create: 2024/11/18 - 下午5:17
// * @Version: v1.0
// */
//@SpringBootTest
//public class SpringDataRedisTest {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Test
//    public void testRedisTemplate() {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        SetOperations setOperations = redisTemplate.opsForSet();
//        ListOperations listOperations = redisTemplate.opsForList();
//        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
//    }
//
//    @Test
//    public void testRedisString() {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("city", "北京");
//        System.out.println(valueOperations.get("city"));
//        valueOperations.set("code", "验证码", 30, TimeUnit.SECONDS);
//        System.out.println(valueOperations.get("code"));
//        valueOperations.setIfAbsent("lock", "1");
//        valueOperations.setIfAbsent("lock", "2");
//    }
//
//    @Test
//    public void testRedisHash() {
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        hashOperations.put("100", "name", "Tom");
//        hashOperations.put("100", "age", 20);
//        System.out.println(hashOperations.keys("100"));
//        System.out.println(hashOperations.values("100"));
//        hashOperations.delete("100", "age");
//    }
//}
