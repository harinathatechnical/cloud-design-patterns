package org.example.cache_aside_pattern;

import com.github.benmanes.caffeine.cache.Cache;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class MyService {

    private final Cache<String, MyEntity> cache;
    private final Store store;

    public MyService(Cache<String, MyEntity> cache, StoreImpl  store) {
        this.cache = cache;
        this.store = store;
    }


    public MyEntity getMyEntity(int id) {
        String key = String.format("StoreWithCache_GetAsync_%d", id);
        Duration expiration = Duration.ofMinutes(3);
        boolean cacheException = false;

        try {
            MyEntity cachedEntity = cache.getIfPresent(key);
            if (cachedEntity != null) {
                return cachedEntity;
            }
        } catch (Exception e) {
            // Cache-related exception: ignore cache for this call
            cacheException = true;
        }

        // Cache miss: get the entity from the underlying store (mocked here)
        MyEntity entity = fetchEntityFromStore(id);

        if (!cacheException) {
            try {
                if (entity != null) {
                    // Put in cache with expiration
                    cache.put(key, entity);
                    // Note: Some cache libraries like Caffeine let you specify per-entry TTL,
                    // but with others you may configure expiry globally at cache config.
                }
            } catch (Exception e) {
                // Ignore cache exceptions
            }
        }

        return entity;
    }

    private MyEntity fetchEntityFromStore(int id) {
        // Mock DB call — replace with real DB lookup
        return new MyEntity(id, "Sample Data");
    }

    public CompletableFuture<Void> updateEntityAsync(MyEntity entity) {
        // Update the object in the original data store asynchronously
        return store.updateEntityAsync(entity)
                .thenRun(() -> {
                    // Get the cache key
                    String key = getAsyncCacheKey(entity.getId());
                    // Invalidate the cache entry
                    cache.invalidate(key);
                });
    }

    private String getAsyncCacheKey(int objectId) {
        return String.format("StoreWithCache_GetAsync_%d", objectId);
    }
}
