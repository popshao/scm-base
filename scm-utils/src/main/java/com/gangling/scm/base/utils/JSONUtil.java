package com.gangling.scm.base.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by ZhouChenmin on 2018/10/8.
 */
@Slf4j
public class JSONUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //序列化的规则，包含非null属性，即空串输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 序列化时，忽略空的bean(即沒有任何Field)
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 序列化时，忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // make all member fields serializable without further annotations,
        // instead of just public fields (default setting).
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // null替换为""
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
//                arg1.writeString("");
//            }
//        });
    }

    private static final boolean userFastjson = true;


    public static String writeValueAsString(Object value) {

        try {
            return objectMapper.writeValueAsString(value);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printPretty(Object value) {
        log.info(writeValueAsString(value));
    }

    public static <T> T readValue(String jsonString, Class<T> tClass) {

        try {
            return objectMapper.readValue(jsonString, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(byte[] source, Class<T> tClass) {

        try {
            return objectMapper.readValue(source, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
