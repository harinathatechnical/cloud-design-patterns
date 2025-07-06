package org.example.cache_aside_pattern;

import java.util.concurrent.CompletableFuture;

public interface Store {
    CompletableFuture<Void> updateEntityAsync(MyEntity entity);
}
