package com.mx.rpc.registry;

/**
 * Server服务注册中心
 *
 * @author mx
 * @date 2019/8/14 2:56 PM
 */
public interface ServiceRegistry {

    /**
     * 服务注册
     * @param serviceName 服务名称
     * @param serviceAddr 服务地址
     * @return
     */
    void register(String serviceName, String serviceAddr);
}
