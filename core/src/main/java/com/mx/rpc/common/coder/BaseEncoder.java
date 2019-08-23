package com.mx.rpc.common.coder;

import com.mx.rpc.common.message.Message;
import com.mx.rpc.util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器
 *
 * @author mx
 * @date 2019/8/14 10:42 PM
 */
public class BaseEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        MessageUtil.messageTransToByteBuf(message, byteBuf);
    }
}
