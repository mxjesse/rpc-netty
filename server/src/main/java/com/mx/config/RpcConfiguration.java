package com.mx.config;

import com.mx.rpc.registry.ServiceRegistry;
import com.mx.rpc.registry.ZookeeperRegistry;
import com.mx.rpc.server.DefaultRpcServer;
import com.mx.rpc.server.SpringRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mx
 * @date 2019/8/22 2:16 PM
 */
@Configuration
public class RpcConfiguration {


    @Value("${rpc.registry.ip}")
    private String registryIp;

    @Value("${rpc.registry.port}")
    private int registryPort;

    @Value("${rpc.server.ip}")
    private String serverIp;

    @Value("${rpc.server.port}")
    private int serverPort;

    @Bean
    public ServiceRegistry serviceRegistry() {

        return new ZookeeperRegistry(registryIp, registryPort);
    }

    @Bean
    public DefaultRpcServer initRpcServer(@Autowired ServiceRegistry serviceRegistry) {

        return new SpringRpcServer();
    }
}
