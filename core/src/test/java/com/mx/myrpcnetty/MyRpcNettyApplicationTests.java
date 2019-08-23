package com.mx.myrpcnetty;

import com.mx.rpc.rpc.RpcFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MyRpcNettyApplicationTests {

    @Test
    public void contextLoads() {

        byte i = (byte) 1;
        System.out.println(i);
    }

    public void test() {

        RpcFuture<String> future = new RpcFuture<String>() {

            @Override
            public String get() {
                return null;
            }

            @Override
            public String get(long time, TimeUnit timeUnit) throws Exception {
                return null;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public boolean cancel() {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        };
    }
}
