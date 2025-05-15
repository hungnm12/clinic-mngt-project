package com.example.schedulerservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

@Component
@Slf4j
public class JsonUtils {
    public static ObjectMapper MAPPER = new ObjectMapper();
    public static ObjectMapper PRETTY_MAPPER = new ObjectMapper();
    private static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        PRETTY_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static byte[] json(Object object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not marshal object as JSON: " + object.getClass().getName(), e);
        }
    }

    public static String asString(Object object) {
        try {
            return (object != null) ? MAPPER.writeValueAsString(object) : "NULL";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not marshal object as JSON: " + object.getClass().getName(), e);
        }
    }

    public static String marshalJsonAsPrettyString(Object object) {
        try {
            return (object != null) ? PRETTY_MAPPER.writeValueAsString(object) : "NULL";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not marshal object as JSON: " + object.getClass().getName(), e);
        }
    }

    public static <T> T unJson(byte[] json, Class<T> type) {
        int length = 0;
        try {
            length = json.length;
            return MAPPER.readValue(json, type);
        } catch (Throwable e) {
            return null;
        }
    }

    public static <T> T unJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Throwable e) {
            return null;
        }
    }

    public static <T> void updateIfNonNull(T oldObj, T newObj, Class<T> tClazz) {
        Field[] fields = tClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(newObj);
                if (newValue != null) {
                    field.set(oldObj, newValue);
                }
            } catch (IllegalAccessException e) {
            }
        }
    }

    public static <T> byte[] serializeJSON(T object) throws IOException {
        return objectMapper.writeValueAsBytes(object);
    }

    public static <T> T deserializeJSON(byte[] json, TypeReference<T> tClass) throws IOException {
        return objectMapper.readValue(json, tClass);
    }

    public static <T> T map2Object(@SuppressWarnings("rawtypes") Map map, TypeReference<T> o) throws IOException {
        return objectMapper.convertValue(map, o);
    }
}
