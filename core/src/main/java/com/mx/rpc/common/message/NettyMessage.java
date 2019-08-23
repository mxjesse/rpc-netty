package com.mx.rpc.common.message;

/**
 * RPC消息类
 *
 * @author mx
 * @date 2019/8/14 4:25 PM
 */
public class NettyMessage implements Message {

    /**
     * 协议首部
     */
    private final Header header;

    /**
     * 协议内容
     */
    private final Body body;

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean isEmptyBody() {
        return body == null;
    }

    public NettyMessage(Header header) {
        this(header, null);
    }

    public NettyMessage(Header header, Body body) {
        this.header = header;
        this.body = body;
    }
}
