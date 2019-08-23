package com.mx.rpc.common.serializer;

import com.mx.rpc.common.exception.SerializationException;

/**
 * 序列化接口
 *
 * @author mx
 * @date 2019/8/14 5:08 PM
 */
public interface Serializer {

    <T> byte[] serialize(T obj) throws SerializationException;

    <T> T deSerialize(byte[] bytes, Class<T> clazz) throws SerializationException;
}
