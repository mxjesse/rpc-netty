package com.mx.rpc.util;

import com.mx.rpc.common.exception.SerializationException;
import com.mx.rpc.common.message.Body;
import com.mx.rpc.common.serializer.Serializer;
import com.mx.rpc.enums.SerializerTypeEnum;

/**
 * 序列化工具类
 *
 * @author mx
 * @date 2019/8/14 6:31 PM
 */
public class SerializerUtil {

    /**
     * 序列化 (对象 -> 字节数组)
     * @param obj body对象
     * @param type 序列化方式Type
     * @return
     * @throws SerializationException
     */
    public static byte[] serialize(Body obj, byte type) throws SerializationException {
        SerializerTypeEnum serializerTypeEnum = SerializerTypeEnum.get(type);
        Serializer serializer = serializerTypeEnum.getSerializer();
        return serializer.serialize(obj);
    }

    /**
     * 反序列化 (字节数组 -> 对象)
     *
     * @param data 字节数组数据
     * @param clazz 反序列化Class对象
     * @param type 序列化方式Type
     * @param <T>
     * @return
     */
    public static <T> T deSerialize(byte[] data, Class<T> clazz, byte type) throws SerializationException {
        SerializerTypeEnum serializerTypeEnum = SerializerTypeEnum.get(type);
        Serializer serializer = serializerTypeEnum.getSerializer();
        return serializer.deSerialize(data, clazz);
    }
}
