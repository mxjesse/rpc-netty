package com.mx.rpc.enums;

/**
 * 消息类型枚举类
 *
 * @author mx
 * @date 2019/8/14 4:28 PM
 */
public enum MessageTypeEnum {

    /**
     * 心跳请求
     */
    HEART_BEAT_REQUEST("heartBeatRequest", (byte) 1),
    /**
     * 心跳响应
     */
    HEART_BEAT_RESPONSE("heartBeatResponse", (byte) 2),
    /**
     * RPC请求
     */
    RPC_REQUEST("rpcRequest", (byte) 4),
    /**
     * RPC响应
     */
    RPC_RESPONSE("rpcResponse", (byte) 8);

    private String name;
    private byte value;

    MessageTypeEnum(String name, byte value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public byte getValue() {
        return value;
    }
}
