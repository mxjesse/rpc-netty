package com.mx.rpc.client.handler;

import com.mx.rpc.client.ReConnectListener;
import com.mx.rpc.common.config.DefaultClientConfigration;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author mx
 * @date 2019/8/19 8:06 PM
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final ClientChannelHandler clientChannelHandler = new ClientChannelHandler();

    private ClientConnectWatcher watcher;

    private DefaultClientConfigration configration;

    public ClientChannelInitializer(DefaultClientConfigration configration, ReConnectListener reConnectListener) {
        this.configration = configration;
        this.watcher = new ClientConnectWatcher(reConnectListener);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new IdleStateHandler(configration.getReadIdle(), configration.getWriteIdle(), 0, TimeUnit.MILLISECONDS))
                // 出方向编码
                .addLast(configration.getCoder().newEncoder())
                // 入方向编码
                .addLast(configration.getCoder().newEncoder())
                // 连接监视器
                .addLast(watcher)
                // 业务处理
                .addLast(clientChannelHandler);
    }
}
