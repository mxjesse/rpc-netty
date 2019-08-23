package com.mx.rpc.rpc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 用于Rpc框架的异步调用
 *
 * @author mx
 * @date 2019/8/16 10:10 AM
 */
public class DefaultRpcFuture implements RpcFuture<RpcResponse> {

    /**
     * 请求id
     */
    private int requestId;

    /**
     * 异步调用完成
     */
    private boolean done = false;

    /**
     * 异步调用取消
     */
    private boolean cancelled = false;

    /**
     * 异步调用结果
     */
    private RpcResponse result;

    public DefaultRpcFuture(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public RpcResponse get() throws InterruptedException {
        synchronized (this) {
            if (this.cancelled) {
                return null;
            }
            while (!this.done) {
                this.wait();
            }

            return this.result;
        }
    }

    @Override
    public RpcResponse get(long time, TimeUnit timeUnit) throws Exception {
        synchronized (this) {
            if (this.cancelled) {
                return null;
            }
            long mills = timeUnit.toMillis(time);
            if (!this.done && mills > 0L) {
                this.wait(mills);
            }
        }

        if (!this.done) {
            throw new TimeoutException("调用超时");
        }

        return this.result;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public boolean cancel() {
        synchronized (this) {
            this.notifyAll();
            this.cancelled = true;
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public void done(RpcResponse result) {
        if (this.cancelled) return;

        this.result = result;
        this.done = true;

        synchronized (this) {
            this.notifyAll();
        }
    }

    public int getRequestId() {
        return requestId;
    }
}
