package com.mx.rpc.common.coder;

import io.netty.channel.ChannelHandler;

/**
 * 默认编解码器
 *
 * @author mx
 * @date 2019/8/18 10:08 PM
 */
public class DefaultCoder implements Coder {

    private static DefaultCoder coder = new DefaultCoder();

    public DefaultCoder() {
    }

    public static DefaultCoder getInstance() {
        return coder;
    }

    @Override
    public ChannelHandler newDecoder() {
        return new BaseDecoder();
    }

    @Override
    public ChannelHandler newEncoder() {
        return new BaseEncoder();
    }
}
