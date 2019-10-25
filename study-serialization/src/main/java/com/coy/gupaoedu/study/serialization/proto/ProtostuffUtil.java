package com.coy.gupaoedu.study.serialization.proto;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dubbo 序列化模块中有针对 protostuff 的实现。
 *
 * 注意：在使用 Protostuff 对实体进行序列化和反序列化时，在已有实体中添加字段必须放到最后去，或者通过 @Tag 来指定字段的序列化顺序，否则反序列化会报错
 *
 * @author chenck
 * @date 2019/10/21 9:57
 */
public class ProtostuffUtil {
    private static Logger log = LoggerFactory.getLogger(ProtostuffUtil.class);
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    public ProtostuffUtil() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> byte[] serializer(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return GraphIOUtil.toByteArray(obj, schema, buffer);
            //return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            log.error("protobuf序列化失败");
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        try {
            T message = (T) objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            GraphIOUtil.mergeFrom(bytes, message, schema);
            //ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            log.error("protobuf反序列化失败");
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
