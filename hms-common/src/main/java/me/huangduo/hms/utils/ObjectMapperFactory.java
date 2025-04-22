package me.huangduo.hms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class ObjectMapperFactory {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = createObjectMapper();
    }

    private ObjectMapperFactory() {
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 设置 LocalDateTime 的格式
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true); // 设置枚举序列化方式
        // 设置全局时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return objectMapper;
    }
}
