package com.mx.rpc.server;

import com.mx.rpc.common.NamedThreadFactory;
import com.mx.rpc.common.message.Message;
import com.mx.rpc.enums.MessageTypeEnum;
import com.mx.rpc.enums.RpcStatesEnum;
import com.mx.rpc.enums.SerializerTypeEnum;
import com.mx.rpc.rpc.RpcRequest;
import com.mx.rpc.rpc.RpcResponse;
import com.mx.rpc.util.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认任务处理类
 *
 * @author mx
 * @date 2019/8/19 12:20 AM
 */
public class DefaultTaskExecutor implements TaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskExecutor.class);

    /**
     * 业务线程池,用来处理用户业务
     */
    private ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1000), new NamedThreadFactory());

    /**
     * 用来处理注册的服务实例
     */
    private Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void execute(ChannelHandlerContext ctx, RpcRequest rpcRequest, SerializerTypeEnum serializerType) {

        threadPool.execute(() -> {
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(rpcRequest.getRequestId());

            try {
                // 调用服务
                Object result = this.handle(rpcRequest);
                rpcResponse.setResult(result);
            } catch (ReflectiveOperationException e) {
                rpcResponse.setErrorMsg(String.format("errorCode: %s, errorMsg: %s, cause: %s",
                        RpcStatesEnum.FAIL.getCode(), RpcStatesEnum.FAIL.getMsg(), e.getCause()));
            }

            Message message = MessageUtil.createMessage(MessageTypeEnum.RPC_RESPONSE, serializerType, rpcResponse);

            // 写回message
            ctx.writeAndFlush(message);
            logger.info("执行完毕: {}", rpcRequest.toString());
        });
    }

    @Override
    public void addInstance(String servierName, Object object) {
        this.serviceMap.put(servierName, object);
    }

    /**
     * 根据请求调用已有的服务
     *
     * @param rpcRequest
     * @return
     * @throws ReflectiveOperationException
     */
    private Object handle(RpcRequest rpcRequest) throws ReflectiveOperationException {

        // 获取服务对象
        String serviceName = rpcRequest.getInterfaceName();
        Object serviceBean = this.getServiceBean(serviceName);
        // 利用反射调用服务
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        return this.invoke(serviceBean, methodName, parameterTypes, parameters);
    }

    /**
     * 利用JDK反射机制调用服务
     *
     * @param serviceBean
     * @param methodName
     * @param parameterTypes
     * @param paramters
     * @return
     */
    private Object invoke(Object serviceBean, String methodName, Class<?>[] parameterTypes, Object[] paramters) throws ReflectiveOperationException {

        Class<?> serviceBeanClass = serviceBean.getClass();
        Method method = serviceBeanClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, paramters);
    }

    /**
     * 根据服务名获取实例
     *
     * @param serviceName
     * @return
     */
    private Object getServiceBean(String serviceName) {
        Object serviceBean = this.serviceMap.get(serviceName);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("not found service bean by name: %S", serviceName));
        }
        return serviceBean;
    }
}
