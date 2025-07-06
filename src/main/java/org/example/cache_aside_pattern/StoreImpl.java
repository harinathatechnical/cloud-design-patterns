package org.example.cache_aside_pattern;

import java.util.concurrent.CompletableFuture;

public class StoreImpl implements Store {

    @Override
    public CompletableFuture<Void> updateEntityAsync(MyEntity entity) {
        return CompletableFuture.runAsync(() -> {
            // Simulate DB update
            System.out.println("Updating entity in DB: " + entity);

            // Simulate some I/O delay
            try {
                Thread.sleep(500); // 500 ms delay to mimic DB call
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            System.out.println("Entity updated: " + entity);
        });
    }
}
