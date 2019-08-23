package com.mx.rpc.registry;

/**
 * @author mx
 * @date 2019/8/19 11:29 PM
 */
public interface ServiceDiscovery {

    /**
     * 服务发现
     *
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
