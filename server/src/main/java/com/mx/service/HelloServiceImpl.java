package com.mx.service;

import com.mx.api.HelloService;
import com.mx.rpc.annotation.RpcServer;
import org.springframework.stereotype.Service;

/**
 * @author mx
 * @date 2019/8/22 2:04 PM
 */
@Service
@RpcServer(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String content) {

        String out = "Hello" + content;
        System.out.println(out);
        return out;
    }
}
