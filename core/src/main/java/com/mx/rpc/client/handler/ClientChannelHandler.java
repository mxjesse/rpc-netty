package com.mx.rpc.client.handler;

import com.mx.rpc.common.channel.ChannelDataHolder;
import com.mx.rpc.rpc.DefaultRpcFuture;
import com.mx.rpc.rpc.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mx
 * @date 2019/8/19 9:45 PM
 */
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);

    public ClientChannelHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {

        int requestId = rpcResponse.getRequestId();
        DefaultRpcFuture defaultRpcFuture = ChannelDataHolder.removeRpcFuture(channelHandlerContext.channel(), requestId);

        if (defaultRpcFuture != null && !defaultRpcFuture.isCancelled()) {
            // 更新future
            defaultRpcFuture.done(rpcResponse);
        } else {
            logger.error("DefaultRpcFuture : requestId - {} not exists", requestId);
            throw new Exception("DefaultRpcFuture not exists");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.channel().close();
    }
}
