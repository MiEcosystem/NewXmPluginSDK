
package com.xiaomi.smarthome.common.plug.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {

    public static void initialize() {
        executor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    private static ThreadPoolExecutor executor;

    /**
     * 在后台线程池中执行runnable所代表的任务。 目前只持续一些独立的，彼此间没有依赖和同步关系的任务。
     * 
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
