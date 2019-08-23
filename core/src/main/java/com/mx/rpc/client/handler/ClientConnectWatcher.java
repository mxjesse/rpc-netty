package com.mx.rpc.client.handler;

import com.mx.rpc.client.Connection;
import com.mx.rpc.client.ReConnectListener;
import com.mx.rpc.common.channel.ChannelDataHolder;
import com.mx.rpc.common.message.Message;
import com.mx.rpc.enums.MessageTypeEnum;
import com.mx.rpc.util.MessageUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author mx
 * @date 2019/8/19 8:11 PM
 */
@ChannelHandler.Sharable
public class ClientConnectWatcher extends SimpleChannelInboundHandler<Message> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnectWatcher.class);

    private ReConnectListener reConnectListener;

    public ClientConnectWatcher(ReConnectListener reConnectListener) {
        this.reConnectListener = reConnectListener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        byte messageType = message.getHeader().messageType();

        // 如果是心跳响应则直接返回,如果不是则交给下一个handler处理
        if (!message.isEmptyBody() && MessageTypeEnum.RPC_RESPONSE.getValue() == messageType) {
            logger.info("Received RPC Response from server: {}", message);
            channelHandlerContext.fireChannelRead(message.getBody());
            // 心跳响应
        } else if (message.isEmptyBody() && MessageTypeEnum.HEART_BEAT_RESPONSE.getValue() == messageType) {
            logger.info("Received HeartBeat Response from Server: {}", message);
        } else {
            logger.info("Received error Message Type: {}", message);
        }
    }

    @Override
    public void run() {
        this.reconnect(Connection.DEFAULT_CONNECT_TIMEOUT);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 当channel关闭时,清除eventLoop中channel对应的future
        ChannelDataHolder.removeChannel(ctx.channel());
        logger.info("Channel closed, try to reconnect.");

        // 线程开启定时任务,尝试重连
        ctx.channel().eventLoop().schedule(this, 3L, TimeUnit.SECONDS);
        ctx.fireChannelInactive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    closeChannel(ctx);
                    break;
                case WRITER_IDLE:
                    // 写入心跳请求
                    ctx.writeAndFlush(MessageUtil.createHeartBeatRequestMessage());
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void reconnect(int connectTimeOut) {
        Connection connection = reConnectListener.getConnection();
        Bootstrap bootstrap = connection.getBootstrap();
        ChannelFuture future = bootstrap.connect(connection.getTargetIp(), connection.getTargetPort());

        //不能在EventLoop中进行同步调用，这样会导致调用线程即EventLoop阻塞
        future.addListener(reConnectListener);
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        Connection connection = reConnectListener.getConnection();
        connection.unbind();
        ctx.close();
    }
}
