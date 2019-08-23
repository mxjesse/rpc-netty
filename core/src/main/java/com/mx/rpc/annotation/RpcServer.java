package com.mx.rpc.annotation;

import javax.annotation.Resource;
import java.lang.annotation.*;

/**
 * @author mx
 * @date 2019/8/22 3:13 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcServer {

    Class<?> value();

    String version() default "";
}
