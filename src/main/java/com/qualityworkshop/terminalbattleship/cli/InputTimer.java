package com.qualityworkshop.terminalbattleship.cli;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InputTimer {

    private final ExecutorService executor;
    private final long timeoutSeconds;

    public InputTimer(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public String readWithTimeout(Callable<String> inputTask) throws TimeoutException {
        Future<String> future = executor.submit(inputTask);
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error reading input", e);
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}