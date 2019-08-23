package com.mx.rpc.common.channel;

import com.mx.rpc.rpc.DefaultRpcFuture;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mx
 * @date 2019/8/16 12:54 PM
 */
public class ChannelData {

    private static final int FUTURE_MAP_INIT_SIZE = 32;

    private Channel channel;

    private long lastReceive;

    private Map<Integer, DefaultRpcFuture> rpcFutureMap = new HashMap<>(FUTURE_MAP_INIT_SIZE);

    public ChannelData(Channel channel, long lastReceive) {
        this.channel = channel;
        this.lastReceive = lastReceive;
    }

    public void update(long lastReceive) {
        this.lastReceive = lastReceive;
    }

    public DefaultRpcFuture getRpcFuture(Integer id) {
        return this.rpcFutureMap.get(id);
    }

    public DefaultRpcFuture removeRpcFuture(Integer id) {
        return this.rpcFutureMap.remove(id);
    }

    public void putRpcFuture(DefaultRpcFuture rpcFuture) {
        this.rpcFutureMap.put(rpcFuture.getRequestId(), rpcFuture);
    }

    public boolean isExpire(int channelAliveTime) {
        return System.currentTimeMillis() - lastReceive >= channelAliveTime;
    }
}
