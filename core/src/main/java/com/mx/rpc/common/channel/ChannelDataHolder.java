package com.mx.rpc.common.channel;

import com.mx.rpc.rpc.DefaultRpcFuture;
import com.mx.rpc.rpc.RpcFuture;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mx
 * @date 2019/8/16 1:05 PM
 */
public class ChannelDataHolder {

    private static int CHANNEL_INIT_SIZE = 8;
    private static ThreadLocal<Map<Channel, ChannelData>> CACHE = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(ChannelDataHolder.class);

    private EventLoop eventLoop;

    public ChannelDataHolder(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    /**
     * 更新channel的最近请求接收时间
     *
     * @param channel
     */
    public static void updateChannel(Channel channel) {

        long lastReveive = System.currentTimeMillis();
        Map<Channel, ChannelData> cache = ChannelDataHolder.checkCache();
        ChannelData channelData = cache.get(channel);
        if (channelData == null) {
            channelData = new ChannelData(channel, lastReveive);
            cache.put(channel, channelData);
        }
        channelData.update(lastReveive);
        logger.info("channel: {} updated.", channel);
    }

    /**
     * 检查缓存中是否有内容,
     * 如果为null:则放入一个新的Size为 {@link CHANNEL_INIT_SIZE} 的Map
     *
     * @return
     */
    public static Map<Channel, ChannelData> checkCache() {

        Map<Channel, ChannelData> cache = CACHE.get();

        if (cache == null) {
            cache = new HashMap<>(CHANNEL_INIT_SIZE);
            CACHE.set(cache);
        }
        return cache;
    }

    /**
     * 清除EventLoop中所有的缓存
     */
    public void removeAll() {
        CACHE.remove();
    }

    /**
     * 判断channel是否过期,如过期则关闭并删除channel,EventLoop线程执行
     *
     * @param channel
     * @param channelAliveTime
     * @return
     */
    public static boolean removeIfExpire(Channel channel, int channelAliveTime) {
        boolean removed = false;

        Map<Channel, ChannelData> cache = ChannelDataHolder.checkCache();
        ChannelData channelData = cache.get(channel);
        if (channelData == null) {
            return true;
        }

        if (channelData.isExpire(channelAliveTime)) {
            if (channel.isOpen()) {
                channel.close();
            }
            removed = true;
            logger.info("channel: {} is expired and closed.", channel);
        }

        return removed;
    }

    /**
     * 调用异常时,清除EventLoop中的future
     *
     * @param channel
     * @param id
     * @return
     */
    public static DefaultRpcFuture removeRpcFuture(Channel channel, Integer id) {

        Map<Channel, ChannelData> cache = checkCache();
        ChannelData channelData = cache.get(channel);
        if (channelData == null) {
            channelData = new ChannelData(channel, System.currentTimeMillis());
            cache.put(channel, channelData);
        }
        return channelData.removeRpcFuture(id);
    }

    public static void removeChannel(Channel channel) {
        Map<Channel, ChannelData> cache = checkCache();
        cache.remove(channel);
    }
}

