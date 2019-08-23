package com.mx.rpc.client;

import com.mx.rpc.registry.ServiceDiscovery;

import javax.management.ServiceNotFoundException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mx
 * @date 2019/8/19 11:32 PM
 */
public abstract class AbsRpcProxy implements RpcProxy {

    private ServiceDiscovery serviceDiscovery;

    /**
     * rpc代理对象列表
     */
    private ConcurrentHashMap<String, Object> serviceInstanceMap = new ConcurrentHashMap<>(16);

    public AbsRpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public <T> T getService(Class<? extends T> interfaceClass) throws ServiceNotFoundException {
        String className = interfaceClass.getName();
        Object instance = this.serviceInstanceMap.get(className);
        // 如果找到实例则返回
        if (instance != null) return (T) instance;
        // 如果没找到实例,则寻找服务地址
        String addr = this.getServiceAddr(className);
        if (addr == null || "".equals(addr)) {
            throw new ServiceNotFoundException();
        }
        // 创建实例对象
        T serviceInstance = this.createInstance(interfaceClass, addr);
        if (serviceInstance == null) return null;
        this.serviceInstanceMap.put(className, serviceInstance);
        return serviceInstance;
    }

    /**
     * 创建一个已注册的服务实例
     *
     * @param interfaceClass
     * @param addr
     * @param <T>
     * @return
     */
    protected abstract <T> T createInstance(Class<? extends T> interfaceClass, String addr);

    /**
     * 通过服务名称寻找注册的服务地址
     *
     * @param className
     * @return
     */
    protected String getServiceAddr(String className) {
        String addr = this.serviceDiscovery.discover(className);
        if (addr == null || "".equals(addr)) return "";
        return addr;
    }
}
