package me.huangduo.hms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.huangduo.hms.enums.ErrorCode;
import me.huangduo.hms.enums.RoleType;

import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(ErrorCode.SYSTEM_ERROR_002.getMessage());
        System.out.println(RoleType.SYSTEM_ROLE.getValue());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置全局时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

    }
}
