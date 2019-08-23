package com.mx.rpc.server.handler;

import com.mx.rpc.common.message.Message;
import com.mx.rpc.enums.SerializerTypeEnum;
import com.mx.rpc.rpc.RpcRequest;
import com.mx.rpc.server.TaskExecutor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mx
 * @date 2019/8/18 10:18 PM
 */
@ChannelHandler.Sharable //只有一个此handler实例
public class RpcServeChannelHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServeChannelHandler.class);

    private TaskExecutor taskExecutor;

    public RpcServeChannelHandler(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        logger.info("服务端接收到 rpc request: {}", message.getBody().toString());
        // 交给线程池执行
        SerializerTypeEnum serializerType = SerializerTypeEnum.get(message.getHeader().serializeType());
        taskExecutor.execute(channelHandlerContext, (RpcRequest) message.getBody(), serializerType);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}
