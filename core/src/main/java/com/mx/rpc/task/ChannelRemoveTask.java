package com.mx.rpc.task;

import com.mx.rpc.common.channel.ChannelDataHolder;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Channel定时清除任务: 删除无效的channel,EventLoop线程运行
 * @author mx
 * @date 2019/8/18 8:11 PM
 */
public class ChannelRemoveTask implements Runnable {

    private Channel channel;
    private int channelAliveTime;
    private EventLoop eventLoop;

    public ChannelRemoveTask(Channel channel, int channelAliveTime, EventLoop eventLoop) {
        this.channel = channel;
        this.channelAliveTime = channelAliveTime;
        this.eventLoop = eventLoop;
    }

    @Override
    public void run() {

        // 如果channel已经removed,则直接返回
        if (ChannelDataHolder.removeIfExpire(channel, channelAliveTime)) return;
        // 如果未removed,则每隔channelAliveTime时间定期检查channel是否需要移除
        this.eventLoop.schedule(this, channelAliveTime, TimeUnit.MILLISECONDS);
    }
}
