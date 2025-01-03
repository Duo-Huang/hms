package me.huangduo.hms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(HmsErrorCodeEnum.SYSTEM_ERROR_001.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置全局时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Test test = new Test(objectMapper);

        test.doTest();

    }
}
