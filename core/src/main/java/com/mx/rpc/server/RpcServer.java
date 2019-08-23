package com.mx.rpc.server;

/**
 * RPC服务端起始接口
 *
 * @author mx
 * @date 2019/8/14 2:27 PM
 */
public interface RpcServer {

    /**
     * RPC服务端启动
     */
    void start();

    /**
     * 注册服务实例
     * @param serviceName
     * @param serviceBean
     * @return
     */
    boolean registerServer(String serviceName, Object serviceBean);


}
