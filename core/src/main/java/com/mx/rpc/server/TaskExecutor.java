package com.mx.rpc.server;

import com.mx.rpc.enums.SerializerTypeEnum;
import com.mx.rpc.rpc.RpcRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * 任务处理类
 *
 * @author mx
 * @date 2019/8/19 12:08 AM
 */
public interface TaskExecutor {

    /**
     * 异步执行任务
     *
     * @param ctx
     * @param rpcRequest
     * @param serializerType
     */
    void execute(ChannelHandlerContext ctx, RpcRequest rpcRequest, SerializerTypeEnum serializerType);

    /**
     * 添加服务实例
     *
     * @param servierName
     * @param object
     */
    void addInstance(String servierName, Object object);
}
