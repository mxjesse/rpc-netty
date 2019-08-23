package com.mx.rpc.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mx
 * @date 2019/8/19 11:01 AM
 */
public class ReConnectListener implements ChannelFutureListener {

    private static final Logger logger = LoggerFactory.getLogger(ReConnectListener.class);

    private Connection connection;

    public ReConnectListener(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        Channel channel = channelFuture.channel();
        if (channelFuture.isSuccess()) {
            logger.info("Reconnect success: {}", channel);
            // 重新绑定
            connection.bind(channel);
        } else {
            connection.addRetryCount();
            if (connection.getCount() < connection.DEFAULT_MAX_RETRIES) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.fireChannelInactive();
            } else {
                logger.info("Fail to Reconnect. Retries {} times.", connection.DEFAULT_MAX_RETRIES);
                connection.unbind();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
