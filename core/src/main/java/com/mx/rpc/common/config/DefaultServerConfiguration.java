package com.mx.rpc.common.config;

/**
 * 默认服务端配置
 *
 * @author mx
 * @date 2019/8/14 3:21 PM
 */
public class DefaultServerConfiguration extends Configuration {

    /**
     * 未收到心跳包TCP最大存活时间,单位:ms
     */
    private Integer channelAliveTime = 20000;

    /**
     * 客户端连接请求大小
     */
    private Integer backLog = 1000;

    /**
     * 绑定地址重用模式
     */
    private boolean reuseAddr = true;

    /**
     * keepAlive模式
     */
    private boolean keepAlive = true;

    public DefaultServerConfiguration() {
    }

    public DefaultServerConfiguration(Integer channelAliveTime, Integer backLog, boolean reuseAddr, boolean keepAlive) {
        this.channelAliveTime = channelAliveTime;
        this.backLog = backLog;
        this.reuseAddr = reuseAddr;
        this.keepAlive = keepAlive;
    }

    public Integer getChannelAliveTime() {
        return channelAliveTime;
    }

    public void setChannelAliveTime(Integer channelAliveTime) {
        this.channelAliveTime = channelAliveTime;
    }

    public boolean isReuseAddr() {
        return reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr) {
        this.reuseAddr = reuseAddr;
    }

    public Integer getBackLog() {
        return backLog;
    }

    public void setBackLog(Integer backLog) {
        this.backLog = backLog;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
