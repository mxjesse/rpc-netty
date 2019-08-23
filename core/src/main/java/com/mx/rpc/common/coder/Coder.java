package com.mx.rpc.common.coder;

import io.netty.channel.ChannelHandler;

/**
 * @author mx
 * @date 2019/8/14 3:47 PM
 */
public interface Coder {

    /**
     * 自定义解码器
     * @return
     */
    ChannelHandler newDecoder();

    /**
     * 自定义编码器
     * @return
     */
    ChannelHandler newEncoder();
}
