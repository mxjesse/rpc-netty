package com.mx.rpc.common.message;

/**
 * RPC消息封装
 *
 * @author mx
 * @date 2019/8/14 4:12 PM
 */
public interface Message {

    /**
     * 获取首部
     * @return
     */
    Header getHeader();

    /**
     * 获取内容
     * @return
     */
    Body getBody();

    /**
     * 判断Body是否为空
     * @return
     */
    boolean isEmptyBody();
}
