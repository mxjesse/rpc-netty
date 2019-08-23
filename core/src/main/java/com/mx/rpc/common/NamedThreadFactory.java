package com.mx.rpc.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义可命名线程工厂类
 *
 * @author mx
 * @date 2019/8/14 2:36 PM
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger groupNum = new AtomicInteger(1);

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final ThreadGroup group;

    private final String NAME_PREFIX;

    private final boolean isDaemon;

    public NamedThreadFactory() {
        this("ThreadPool");
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean isDaemon) {

        this.group = Thread.currentThread().getThreadGroup();

        this.NAME_PREFIX = prefix + "-" + groupNum.getAndIncrement() + "-thread-";
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {

        Thread t = new Thread(group, NAME_PREFIX + threadNum.getAndIncrement());
        t.setDaemon(isDaemon);
        return t;
    }
}
