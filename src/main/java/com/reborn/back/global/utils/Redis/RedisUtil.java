package com.reborn.back.global.utils.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    // 데이터 저장 (유효 시간 지정)
    public void setDataExpire(String key, String value, long duration) {
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    // 데이터 조회
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 데이터 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    /*
    조회수 관련 로직 처리
     */

    // SET 데이터 추가 (중복 없는 저장)
    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    // SET 데이터 조회
    public Set<String> getSetData(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // TTL 설정
    public void expireKey(String key, long duration) {
        redisTemplate.expire(key, duration, TimeUnit.SECONDS);
    }

    // 특정 패턴과 일치하는 모든 키 조회
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}