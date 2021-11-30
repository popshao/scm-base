package com.gangling.scm.base.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ThreadUtils {

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        TraceIdUtil.putTraceId();
        return CompletableFuture.supplyAsync(supplier, ThreadPoolUtils.getIOThreadPoolExecutor());
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        TraceIdUtil.putTraceId();
        return CompletableFuture.runAsync(runnable, ThreadPoolUtils.getIOThreadPoolExecutor());
    }

    public static CompletableFuture<?> composed(CompletableFuture<?>... futures) {
        // 首先构造一个当全部成功则成功的CompletableFuture
        CompletableFuture<?> allComplete = CompletableFuture.allOf(futures);

        // 再构造一个当有一个失败则失败的的CompletableFuture
        CompletableFuture<?> anyException = new CompletableFuture<>();
        for (CompletableFuture<?> completableFuture : futures) {
            completableFuture.exceptionally((t) -> {
                //对于传入的futures列表，如果一个有异常，则把新建的CompletableFuture置为成功
                anyException.completeExceptionally(t);
                return null;
            });
        }
        // 让allComplete和anyException其中有一个完成则完成
        // 如果allComplete有一个异常，anyException会成功完成，则整个就提前完成了
        return CompletableFuture.anyOf(allComplete, anyException);
    }

}
