package com.mx.rpc.server.handler;

import com.mx.rpc.common.channel.ChannelDataHolder;
import com.mx.rpc.common.message.Header;
import com.mx.rpc.common.message.Message;
import com.mx.rpc.enums.MessageTypeEnum;
import com.mx.rpc.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务连接的ChannelHandler
 *
 * @author mx
 * @date 2019/8/18 9:34 PM
 */
public class ServerConnectionChannelHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(ServerConnectionChannelHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        Header header = message.getHeader();
        // 刷新最新请求时间
        ChannelDataHolder.updateChannel(channelHandlerContext.channel());
        // 如果是心跳请求直接返回,如果不是则传给下一个handler
        if (MessageTypeEnum.HEART_BEAT_REQUEST.getValue() == message.getHeader().messageType()) {
            logger.info("服务端收到心跳请求, channel: {} ", channelHandlerContext.channel());
            channelHandlerContext.writeAndFlush(MessageUtil.createHeartBeatResponseMessage());
        } else {
            channelHandlerContext.fireChannelRead(message);
        }
    }
}
