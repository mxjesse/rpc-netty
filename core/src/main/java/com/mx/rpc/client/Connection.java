package com.mx.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

/**
 * @author mx
 * @date 2019/8/19 10:15 AM
 */
public class Connection {

    /**
     * 默认重试次数
     */
    public static final int DEFAULT_MAX_RETRIES = 20;

    /**
     * 默认超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    /**
     * 目标IP
     */
    private String targetIp;

    /**
     * 目标端口
     */
    private int targetPort;

    /**
     * 客户端启动类
     */
    private Bootstrap bootstrap;

    private final int MAX_RETRIES;

    private final int CONNECT_TIMEOUT;

    private int count;

    private Channel channel;

    public void bind(Channel channel) {
        this.channel = channel;
        this.count = 0;
    }

    public void unbind() {
        this.count = 0;
        this.channel = null;
    }

    public void addRetryCount() {
        this.count = count + 1;
    }

    public Connection(String targetIp, int targetPort, Bootstrap bootstrap) {
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        this.bootstrap = bootstrap;
        this.MAX_RETRIES = DEFAULT_MAX_RETRIES;
        this.CONNECT_TIMEOUT = DEFAULT_CONNECT_TIMEOUT;
    }

    public Connection(String targetIp, int targetPort, Bootstrap bootstrap, int MAX_RETRIES, int CONNECT_TIMEOUT) {
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        this.bootstrap = bootstrap;
        this.MAX_RETRIES = MAX_RETRIES;
        this.CONNECT_TIMEOUT = CONNECT_TIMEOUT;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public int getCount() {
        return count;
    }
}
