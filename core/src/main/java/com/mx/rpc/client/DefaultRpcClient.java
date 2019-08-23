package com.mx.rpc.client;

import com.mx.rpc.client.handler.ClientChannelInitializer;
import com.mx.rpc.common.NamedThreadFactory;
import com.mx.rpc.common.channel.ChannelDataHolder;
import com.mx.rpc.common.config.DefaultClientConfigration;
import com.mx.rpc.rpc.DefaultRpcFuture;
import com.mx.rpc.rpc.RpcRequest;
import com.mx.rpc.rpc.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mx
 * @date 2019/8/19 10:00 AM
 */
public class DefaultRpcClient {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRpcClient.class);

    // 计数器阈值
    private static final int THRESHOLD = Integer.MAX_VALUE >> 1;

    /**
     * 客户端NIO线程池
     */
    private static NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("Rpc-client", false));

    private String ip;
    private int port;

    /**
     * 客户端的bootStrap
     */
    private Bootstrap bootstrap;

    /**
     * 客户端连接配置
     */
    private DefaultClientConfigration configration;

    /**
     * 请求id生成器
     */
    private AtomicInteger idCount;

    private Connection connection;

    public DefaultRpcClient(String ip, int port, DefaultClientConfigration configration) {
        this.ip = ip;
        this.port = port;
        this.configration = configration;
        this.init();
    }

    private void init() {
        this.idCount = new AtomicInteger(0);
        this.bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true);
        this.connection = new Connection(ip, port, bootstrap);

        ReConnectListener reConnectListener = new ReConnectListener(connection);
        bootstrap.handler(new ClientChannelInitializer(this.configration, reConnectListener));
    }

    /**
     * 建立channel连接
     *
     * @param ip
     * @param port
     * @param connectTimeOut
     * @return
     * @throws InterruptedException
     * @throws ConnectException
     */
    private Channel doCreateConnection(String ip, int port, int connectTimeOut) throws InterruptedException, ConnectException {

        String addr = ip + ":" + port;
        this.bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut);
        // 建立连接
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        channelFuture.awaitUninterruptibly();
        channelFuture.sync();

        if (!channelFuture.isDone()) {
            logger.warn("Create connection to {} timeout.", addr);
            throw new ConnectException(String.format("Create connection to %s timeout.", addr));
        }

        if (channelFuture.isCancelled()) {
            logger.warn("Create connection to {} cancelled by user.", addr);
            throw new ConnectException(String.format("Create connection to %s cancelled by user", addr));
        }

        if (!channelFuture.isSuccess()) {
            logger.warn("Create connection to {} error.", addr);
            throw new ConnectException(String.format("Create connection to %s error", addr));
        }

        return channelFuture.channel();
    }

    public RpcResponse invokeSync(Method method, Object[] parameters) {
        RpcRequest rpcRequest = this.bulidRpcRequest(method, parameters);
        logger.info("Rpc request: {} by syncMethod", rpcRequest.toString());

        return null;
    }

    /**
     * 实际调用方法
     *
     * @param request
     * @return
     */
    private DefaultRpcFuture invoke(RpcRequest request) {

        DefaultRpcFuture defaultRpcFuture = new DefaultRpcFuture(request.getRequestId());

        Channel channel = null;

        ChannelDataHolder channelDataHolder = null;

        return null;
    }

    /**
     * 构建RpcRequest
     *
     * @param method
     * @param parameters
     * @return
     */
    public RpcRequest bulidRpcRequest(Method method, Object[] parameters) {

        RpcRequest rpcRequest = new RpcRequest();

        if (idCount.get() >= THRESHOLD) {
            synchronized (this) {
                if (idCount.get() >= THRESHOLD) {
                    idCount.getAndSet(0);
                }
            }
        }
        // 设置请求id
        rpcRequest.setRequestId(idCount.getAndIncrement());
        // 设置服务名称
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        // 设置调用方法名称
        rpcRequest.setMethodName(method.getName());
        // 设置参数类型
        rpcRequest.setParameterTypes(method.getParameterTypes());
        // 设置参数
        rpcRequest.setParameters(parameters);

        return rpcRequest;
    }
}
