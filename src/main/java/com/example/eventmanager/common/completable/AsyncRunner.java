package com.example.eventmanager.common.completable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class AsyncRunner {
    private final Executor executor;

    public CompletableFuture<Void> run(Runnable task) {
        return CompletableFuture.runAsync(task, executor)
                .exceptionally(ex -> {
                    log.error("Async execution failed", ex);
                    return null; // fallback
                });
    }
}
