package com.gangling.scm.base.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtils {
    private static AtomicInteger threadIdx = new AtomicInteger(0);
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() + 1,
            1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(512),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
//                    t.setDaemon(true);
                    t.setName("scmThreadPool-" + threadIdx.getAndIncrement());
                    return t;
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());
    private static AtomicInteger threadIdx2 = new AtomicInteger(0);
    private static ThreadPoolExecutor io_executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 50,
            1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(512),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
//                    t.setDaemon(true);
                    t.setName("scmThreadPool-" + threadIdx2.getAndIncrement());
                    return t;
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return executor;
    }

    public static ThreadPoolExecutor getIOThreadPoolExecutor() {
        return io_executor;
    }
}
