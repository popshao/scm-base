package com.gangling.scm.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
public class FunctionUtil {
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> CompletableFuture<T> execInFuture(Supplier<T> supplier, String futureName) {
        CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(supplier, ThreadPoolUtils.getIOThreadPoolExecutor());
        completableFuture.thenAccept(result -> {
            log.info("{}执行成功", futureName);
            MDC.clear();
        });
        return completableFuture;
    }

    public <T> T getFromFuture(CompletableFuture<T> completableFuture, long timeout) {
        try {
            return completableFuture.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("future.get失败", e);
        }
        return null;
    }
}
