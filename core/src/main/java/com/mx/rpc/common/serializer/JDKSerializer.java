package com.mx.rpc.common.serializer;

import com.mx.rpc.common.exception.SerializationException;

import java.io.*;

/**
 * @author mx
 * @date 2019/8/14 5:34 PM
 */
public class JDKSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) throws SerializationException {

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(obj);
            oos.flush();
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializationException();
        }
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) throws SerializationException {

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(is);
            T t = (T) ois.readObject();
            return t;
        } catch (Exception e) {
            throw new SerializationException();
        }
    }
}
