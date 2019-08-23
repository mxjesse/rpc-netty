package com.mx.rpc.util;

import com.mx.rpc.common.exception.SerializationException;
import com.mx.rpc.common.message.*;
import com.mx.rpc.enums.MessageTypeEnum;
import com.mx.rpc.enums.SerializerTypeEnum;
import com.mx.rpc.rpc.RpcRequest;
import com.mx.rpc.rpc.RpcResponse;
import io.netty.buffer.ByteBuf;

/**
 * 请求消息构建工具类
 *
 * @author mx
 * @date 2019/8/14 4:45 PM
 */
public class MessageUtil {

    /**
     * 创建一个请求
     *
     * @param header 首部
     * @param body   内容
     * @return
     */
    public static Message createMessage(Header header, Body body) {
        return new NettyMessage(header, body);
    }

    public static Message createMessage(MessageTypeEnum messageType, SerializerTypeEnum serializerType, Body body) {
        Header header = new DefaultHeader(messageType.getValue(), serializerType.getValue(), 0);
        return createMessage(header, body);
    }

    /**
     * 创建一个心跳请求的Message
     *
     * @return
     */
    public static Message createHeartBeatRequestMessage() {
        return createMessage(MessageTypeEnum.HEART_BEAT_REQUEST, SerializerTypeEnum.DEFAULT, null);
    }

    /**
     * 创建一个心跳响应的Message
     *
     * @return
     */
    public static Message createHeartBeatResponseMessage() {
        return createMessage(MessageTypeEnum.HEART_BEAT_RESPONSE, SerializerTypeEnum.DEFAULT, null);
    }

    /**
     * 解析首部,并封装一个Header返回
     *
     * @param byteBuf
     * @return
     */
    public static Header byteBufTransToHeader(ByteBuf byteBuf) {
        // 协议类型code
        byte protocolCode = byteBuf.readByte();
        // 消息类型code
        byte messageCode = byteBuf.readByte();
        // 序列化类型code
        byte serializationCode = byteBuf.readByte();
        // 消息长度
        int lenth = byteBuf.readInt();

        return new DefaultHeader(protocolCode, messageCode, serializationCode, lenth);
    }

    /**
     * 参照Header首部和数据,解析并返回一个Body对象
     *
     * @param header
     * @param byteBuf
     * @return
     * @throws SerializationException
     */
    public static Body byteBufTransToBody(Header header, ByteBuf byteBuf) throws SerializationException {
        // 1.如果首部显示body长度为0,返回null
        if (header.bodyLength() == 0) return null;
        // 2.body长度不为0
        byte[] data = new byte[header.bodyLength()];
        byteBuf.readBytes(data);

        // RPC请求
        if (MessageTypeEnum.RPC_REQUEST.getValue() == header.messageType()) {
            return SerializerUtil.deSerialize(data, RpcRequest.class, header.serializeType());
        } else {
            return SerializerUtil.deSerialize(data, RpcResponse.class, header.serializeType());
        }
    }

    /**
     * Message对象写入缓冲区
     *
     * @param message
     * @param byteBuf
     */
    public static void messageTransToByteBuf(Message message, ByteBuf byteBuf) throws SerializationException {
        // message封装的Header对象
        Header header = message.getHeader();
        // 初始化bodyLength为0
        int bodyLength = 0;

        byte[] data = null;
        if (!message.isEmptyBody()) {
            data = SerializerUtil.serialize(message.getBody(), header.serializeType());
            bodyLength = data.length;
        }
        header.setBodyLenth(bodyLength);

        headerTransToByteBuf(header, byteBuf);
        bodyTransToByteBuf(data, byteBuf);
    }

    /**
     * Header对象写入缓冲区
     *
     * @param header
     * @param byteBuf
     */
    public static void headerTransToByteBuf(Header header, ByteBuf byteBuf) {

        byteBuf.writeByte(header.protocolCode());
        byteBuf.writeByte(header.messageType());
        byteBuf.writeByte(header.serializeType());
        byteBuf.writeInt(header.bodyLength());
    }

    /**
     * Body对象序列化后的字节写入缓冲区
     *
     * @param data
     * @param byteBuf
     */
    public static void bodyTransToByteBuf(byte[] data, ByteBuf byteBuf) {

        if (data != null) {
            byteBuf.writeBytes(data);
        }
    }
}
