package com.mx.rpc.common.config;

import com.mx.rpc.common.coder.Coder;
import com.mx.rpc.enums.SerializerTypeEnum;

/**
 * @author mx
 * @date 2019/8/19 9:38 AM
 */
public class DefaultClientConfigration extends Configuration {

    /**
     * 客户端最大重试次数
     */
    private Integer maxRetries = 10;

    /**
     * 客户端连接超时时间: ms
     */
    private Integer connectTimeOut = 2000;

    /**
     * 客户端异步调用超时时间: ms
     */
    private Integer rpcRequestTimeOut = 3000;

    /**
     * 读等待时间: ms
     */
    private Integer readIdle = 10000;

    /**
     * 写等待时间: ms
     */
    private Integer writeIdle = 10000;

    /**
     * 序列化类型
     */
    private SerializerTypeEnum serializerTypeEnum = SerializerTypeEnum.DEFAULT;

    public DefaultClientConfigration(Integer maxRetries, Integer connectTimeOut, Integer rpcRequestTimeOut, Integer readIdle, Integer writeIdle, SerializerTypeEnum serializerTypeEnum) {
        this.maxRetries = maxRetries;
        this.connectTimeOut = connectTimeOut;
        this.rpcRequestTimeOut = rpcRequestTimeOut;
        this.readIdle = readIdle;
        this.writeIdle = writeIdle;
        this.serializerTypeEnum = serializerTypeEnum;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Integer connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Integer getRpcRequestTimeOut() {
        return rpcRequestTimeOut;
    }

    public void setRpcRequestTimeOut(Integer rpcRequestTimeOut) {
        this.rpcRequestTimeOut = rpcRequestTimeOut;
    }

    public Integer getReadIdle() {
        return readIdle;
    }

    public void setReadIdle(Integer readIdle) {
        this.readIdle = readIdle;
    }

    public Integer getWriteIdle() {
        return writeIdle;
    }

    public void setWriteIdle(Integer writeIdle) {
        this.writeIdle = writeIdle;
    }

    public SerializerTypeEnum getSerializerTypeEnum() {
        return serializerTypeEnum;
    }

    public void setSerializerTypeEnum(SerializerTypeEnum serializerTypeEnum) {
        this.serializerTypeEnum = serializerTypeEnum;
    }

    @Override
    public Coder getCoder() {
        return super.getCoder();
    }

    @Override
    public void setCoder(Coder coder) {
        super.setCoder(coder);
    }
}
