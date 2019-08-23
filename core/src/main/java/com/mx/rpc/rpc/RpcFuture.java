package com.mx.rpc.rpc;

import java.util.concurrent.TimeUnit;

/**
 *
 * @param <V>
 */
public interface RpcFuture<V> {

    /**
     * 返回执行结果
     *
     * @return
     */
    V get() throws InterruptedException;

    /**
     * 一定时间返回结果
     *
     * @param time
     * @param timeUnit
     * @return
     */
    V get(long time, TimeUnit timeUnit) throws Exception;

    /**
     * 操作是否完成
     *
     * @return
     */
    boolean isDone();

    /**
     * 操作取消
     *
     * @return
     */
    boolean cancel();

    /**
     * 操作是否取消
     *
     * @return
     */
    boolean isCancelled();
}
