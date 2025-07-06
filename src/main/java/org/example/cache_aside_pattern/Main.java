package org.example.cache_aside_pattern;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Cache<String, MyEntity> caffeineCache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();

        MyService service = new MyService(caffeineCache, new StoreImpl());

        MyEntity entity = service.getMyEntity(123);
        System.out.println(entity);
    }
}
