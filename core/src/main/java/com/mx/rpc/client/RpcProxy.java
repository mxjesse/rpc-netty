package com.mx.rpc.client;

import javax.management.ServiceNotFoundException;
import java.lang.reflect.Method;

/**
 * @author mx
 * @date 2019/8/19 11:27 PM
 */
public interface RpcProxy {

    <T> T getService(Class<? extends T> interfaceClass) throws ServiceNotFoundException;

    <T> T call(Class<? extends T> interfaceClass, Method method, Object[] parameters);
}
