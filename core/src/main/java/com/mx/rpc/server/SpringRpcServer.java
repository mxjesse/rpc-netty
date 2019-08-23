package com.mx.rpc.server;

import com.mx.rpc.annotation.RpcServer;
import com.mx.rpc.common.config.DefaultServerConfiguration;
import com.mx.rpc.registry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * @author mx
 * @date 2019/8/23 10:27 AM
 */
public class SpringRpcServer extends DefaultRpcServer implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 默认注册服务的构造
     *
     * @param ip
     * @param port
     * @param serviceRegistry
     */
    public SpringRpcServer(String ip, int port, ServiceRegistry serviceRegistry) {
        super(ip, port, serviceRegistry);
    }

    /**
     * 自定义注册服务的构造
     *
     * @param ip
     * @param port
     * @param serviceRegistry
     * @param configuration
     */
    public SpringRpcServer(String ip, int port, ServiceRegistry serviceRegistry, DefaultServerConfiguration configuration) {
        super(ip, port, serviceRegistry, configuration);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        // TODO why getParent
        if (applicationContext.getParent() == null) {
            // 扫描注解
            Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcServer.class);
            // 注册服务到注册中心中
            this.registerAllService(serviceMap);
            // 启动服务
            this.start();
        }
    }
}
