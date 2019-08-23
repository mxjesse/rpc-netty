package com.mx.rpc.common.coder;

import com.mx.rpc.common.message.Body;
import com.mx.rpc.common.message.Header;
import com.mx.rpc.util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器
 *
 * @author mx
 * @date 2019/8/14 3:49 PM
 */
public class BaseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // 可读取字节少于协议首部字节长度
        if (byteBuf.readableBytes() < Header.PROTOCOL_HEADER_LENGTH) {
            return;
        }
        // 标记当前readIndex位置
        byteBuf.markReaderIndex();
        // 解析首部,获取Header对象
        Header header = MessageUtil.byteBufTransToHeader(byteBuf);
        // 消息体长度如果小于传送过来的Header中的长度,则把readIndex重置到上一步mark的位置
        if (header.bodyLength() != 0 && byteBuf.readableBytes() < header.bodyLength()) {
            byteBuf.resetReaderIndex();
            return;
        }
        // 解析反序列化Body对象
        Body body = MessageUtil.byteBufTransToBody(header, byteBuf);
        // 添加封装的Message对象
        list.add(MessageUtil.createMessage(header, body));
    }
}
