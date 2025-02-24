package me.huangduo.hms.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.huangduo.hms.exceptions.JsonDeserializationException;
import me.huangduo.hms.exceptions.JsonSerializationException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    public static <T> String serialize(T object) throws JsonSerializationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) throws JsonDeserializationException {
        try {
            return json != null ? objectMapper.readValue(json, clazz) : null;
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException(e);
        }
    }
}
