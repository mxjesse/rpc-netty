package com.mx.rpc.server.handler;

import com.mx.rpc.common.coder.Coder;
import com.mx.rpc.task.ChannelRemoveTask;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * 服务ServerChannel初始化
 *
 * @author mx
 * @date 2019/8/14 3:35 PM
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Coder coder;
    private ChannelHandler channelHandler;
    private int channelAliveTime;

    public ServerChannelInitializer(Coder coder, ChannelHandler channelHandler, int channelAliveTime) {
        this.coder = coder;
        this.channelHandler = channelHandler;
        this.channelAliveTime = channelAliveTime;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        EventLoop eventLoop = socketChannel.eventLoop();
        // 创建定时任务,检查channel是否过期
        eventLoop.schedule(new ChannelRemoveTask(socketChannel, channelAliveTime, eventLoop), channelAliveTime, TimeUnit.MILLISECONDS);

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline // 出方向编码
                .addLast(coder.newEncoder())
                // 入方向编码
                .addLast(coder.newDecoder())
                .addLast(new ServerConnectionChannelHandler())
                // 业务处理
                .addLast(channelHandler);
    }
}
