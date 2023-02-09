package com.example.hydrogen.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class JsonUtils {


    public static String toJSONString(Object obj) {
        ObjectMapper objectMapper = ApplicationContextHolder.getBean(ObjectMapper.class);
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json 字符串处理错误", e);
            throw new RuntimeException();
        }
    }

    public static <T> T parseToObject(String json, Class<T> toClass) {
        ObjectMapper objectMapper = ApplicationContextHolder.getBean(ObjectMapper.class);
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(json, toClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法可以用于复杂对象比如，List<Account>
     */
    public static <T> T parseToObject(String json, TypeReference<T> type) {
        ObjectMapper objectMapper = ApplicationContextHolder.getBean(ObjectMapper.class);
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.error("【parseToObject】failed e: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static class Long2Date extends JsonSerializer<Long> {

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
//            if (value != null) {
//                gen.writeString(DateUtils.format(new Date(value), DateUtils.DTFormat.yyyy_MM_dd_HH_mm_ss));
//            }
        }
    }

    public static class LongToDate extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return new Date(p.getLongValue());
        }
    }
}