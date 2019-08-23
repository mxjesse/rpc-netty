package com.mx.rpc.common.message;

/**
 * 默认自定义协议首部
 *
 * @author mx
 * @date 2019/8/14 4:05 PM
 */
public class DefaultHeader implements Header {

    /**
     * 协议类型 : 1字节
     */
    private final byte protocolType;

    /**
     * 消息类型 : 1字节
     */
    private final byte messageType;

    /**
     * 序列化类型 : 1字节
     */
    private final byte serializeType;

    /**
     * 协议体长度 : 4字节
     */
    private int bodyLength;

    private static final byte PROTOCOL_CODE = (byte) 0xAC;

    public DefaultHeader(byte messageType, byte serializableType, int bodyLength) {
        this(PROTOCOL_CODE, messageType, serializableType, bodyLength);
    }

    public DefaultHeader(byte protocolType, byte messageType, byte serializeType, int bodyLength) {
        this.protocolType = protocolType;
        this.messageType = messageType;
        this.serializeType = serializeType;
        this.bodyLength = bodyLength;
    }

    @Override
    public byte protocolCode() {
        return this.protocolType;
    }

    @Override
    public byte messageType() {
        return this.messageType;
    }

    @Override
    public byte serializeType() {
        return this.serializeType;
    }

    @Override
    public int bodyLength() {
        return this.bodyLength;
    }

    @Override
    public void setBodyLenth(int length) {
        this.bodyLength = length;
    }
}
