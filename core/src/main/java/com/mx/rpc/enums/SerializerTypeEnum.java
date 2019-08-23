package com.mx.rpc.enums;

import com.mx.rpc.common.serializer.JDKSerializer;
import com.mx.rpc.common.serializer.Serializer;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 序列化枚举类
 *
 * @author mx
 * @date 2019/8/14 4:50 PM
 */
public enum SerializerTypeEnum {

    DEFAULT("default", (byte) 1, new JDKSerializer());

    private String name;

    private byte value;

    private Serializer serializer;

    // 静态变量 - 查找响应序列化枚举类型的Map
    private static final Map<Byte, SerializerTypeEnum> LOOK_UP = new HashMap<>();

    static {
        SerializerTypeEnum serializerTypeEnum;
        for (Object m : EnumSet.allOf(SerializerTypeEnum.class)) {
            serializerTypeEnum = (SerializerTypeEnum) m;
            LOOK_UP.put(serializerTypeEnum.getValue(), serializerTypeEnum);
        }
    }

    public static SerializerTypeEnum get(byte value) {
        return LOOK_UP.get(value);
    }

    SerializerTypeEnum(String name, byte value, Serializer serializer) {
        this.name = name;
        this.value = value;
        this.serializer = serializer;
    }

    public String getName() {
        return name;
    }

    public byte getValue() {
        return value;
    }

    public Serializer getSerializer() {
        return serializer;
    }
}
