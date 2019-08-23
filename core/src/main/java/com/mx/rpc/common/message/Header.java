package com.mx.rpc.common.message;

/**
 * 协议首部
 *
 * @author mx
 * @date 2019/8/14 3:51 PM
 */
public interface Header {

    /**
     * 协议首部长度 : 7字节
     */
    int PROTOCOL_HEADER_LENGTH = 7;

    /**
     * 协议序号
     * @return
     */
    byte protocolCode();

    /**
     * 消息类型
     * @return
     */
    byte messageType();

    /**
     * 序列化类型
     * @return
     */
    byte serializeType();

    /**
     * 协议体长度
     * @return
     */
    int bodyLength();

    void setBodyLenth(int length);
}
