package com.mx.rpc.server;

import com.mx.rpc.common.NamedThreadFactory;
import com.mx.rpc.common.config.DefaultServerConfiguration;
import com.mx.rpc.registry.ServiceRegistry;
import com.mx.rpc.server.handler.RpcServeChannelHandler;
import com.mx.rpc.server.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 默认RPC服务启动类
 *
 * @author mx
 * @date 2019/8/14 2:29 PM
 */
public class DefaultRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRpcServer.class);

    private String ip;

    private int port;

    /**
     * 服务注册中心类
     */
    private ServiceRegistry serviceRegistry;

    /**
     * 默认服务端配置项
     */
    private DefaultServerConfiguration configuration;

    /**
     * Netty的连接线程池
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup
            (1, new NamedThreadFactory("Rpc-Server-Boss", false));

    /**
     * Netty的IO线程池
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup
            (Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("Rpc-Server-Worker", true));

    /**
     * 业务处理器
     */
    private TaskExecutor taskExecutor = new DefaultTaskExecutor();

    public DefaultRpcServer(String ip, int port, ServiceRegistry serviceRegistry) {
        this(ip, port, serviceRegistry, new DefaultServerConfiguration());
    }

    public DefaultRpcServer(String ip, int port, ServiceRegistry serviceRegistry, DefaultServerConfiguration configuration) {
        this.ip = ip;
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.configuration = configuration;
    }

    @Override
    public void start() {

        // 启动服务实际方法
        doRunServer();
    }

    @Override
    public boolean registerServer(String serviceName, Object serviceBean) {
        return false;
    }

    /**
     * 注册传入的服务 key beanName, value beanObject
     */
    protected void registerAllService(Map<String, Object> serviceMap) {
        if (serviceMap == null || serviceMap.isEmpty()) {
            logger.error("需要注册的 serviceMap 为空");
        }
        for (Map.Entry<String, Object> entry : serviceMap.entrySet()) {
            this.registerServer(entry.getKey(), entry.getValue());
        }
    }

    private void doRunServer() {


    }

    private ServerBootstrap initServerBootStrap(NioEventLoopGroup bossGroup) {

        return new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //允许server端口reuse
                .option(ChannelOption.SO_REUSEADDR, this.configuration.isReuseAddr())
                .option(ChannelOption.SO_BACKLOG, this.configuration.getBackLog())
                .childOption(ChannelOption.SO_KEEPALIVE, this.configuration.isKeepAlive())
                .childHandler(new ServerChannelInitializer(this.configuration.getCoder(),
                        new RpcServeChannelHandler(this.taskExecutor),
                        this.configuration.getChannelAliveTime()));
    }

}
