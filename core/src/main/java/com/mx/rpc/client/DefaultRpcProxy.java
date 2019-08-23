package com.mx.rpc.client;

import com.mx.rpc.common.config.DefaultClientConfigration;
import com.mx.rpc.registry.ServiceDiscovery;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mx
 * @date 2019/8/19 11:44 PM
 */
public class DefaultRpcProxy extends AbsRpcProxy {

    private DefaultClientConfigration configration;

    private ConcurrentHashMap<String, DefaultRpcClient> rpcClientMap;

    public DefaultRpcProxy(ServiceDiscovery serviceDiscovery) {
        super(serviceDiscovery);
    }

    public DefaultRpcProxy(ServiceDiscovery serviceDiscovery, DefaultClientConfigration configration) {
        super(serviceDiscovery);
        this.configration = configration;
        this.rpcClientMap = new ConcurrentHashMap<>(16);
    }

    @Override
    protected <T> T createInstance(Class<? extends T> interfaceClass, String addr) {
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                (proxy, method, args) -> {
                    String className = interfaceClass.getName();
                    DefaultRpcClient defaultRpcClient = this.getRpcClient(className, addr, false);


                    return null;
                });
        return null;
    }

    @Override
    public <T> T call(Class<? extends T> interfaceClass, Method method, Object[] parameters) {
        return null;
    }

    /**
     * 获取RpcClient对象
     *
     * @param interfaceName
     * @return
     */
    private DefaultRpcClient getRpcClient(String interfaceName) {

        DefaultRpcClient defaultRpcClient = this.rpcClientMap.get(interfaceName);
        if (defaultRpcClient != null) return defaultRpcClient;
        String addr = super.getServiceAddr(interfaceName);
        return this.getRpcClient(interfaceName, addr, true);
    }

    /**
     * 创建RpcClient对象
     *
     * @param interfaceName
     * @param addr
     * @param forceCreate
     * @return
     */
    private DefaultRpcClient getRpcClient(String interfaceName, String addr, boolean forceCreate) {

        DefaultRpcClient defaultRpcClient;
        // 非强制创建,则找到直接返回
        if (!forceCreate && (defaultRpcClient = this.rpcClientMap.get(interfaceName)) != null) {
            return defaultRpcClient;
        }
        synchronized (this) {
            if (!forceCreate && (defaultRpcClient = this.rpcClientMap.get(interfaceName)) != null) {
                return defaultRpcClient;
            }

            String[] addrs = addr.split(":");
            defaultRpcClient = new DefaultRpcClient(addrs[0], Integer.parseInt(addrs[1]), configration);
            this.rpcClientMap.put(interfaceName, defaultRpcClient);
        }
        return defaultRpcClient;
    }
}
