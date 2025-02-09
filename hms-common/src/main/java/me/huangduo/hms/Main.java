package me.huangduo.hms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.huangduo.hms.model.User;
import me.huangduo.hms.enums.ErrorCodeEnum;
import me.huangduo.hms.enums.RoleTypeEnum;
import me.huangduo.hms.events.InvitationEvent;

import java.time.LocalDateTime;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(ErrorCodeEnum.SYSTEM_ERROR_002.getMessage());
        System.out.println(RoleTypeEnum.SYSTEM_ROLE.getValue());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置全局时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        User user = new User(1, "huangduo", "黄铎", LocalDateTime.now(), LocalDateTime.now());

        InvitationEvent invitationEvent = new InvitationEvent(null, 1, user, "fdsfa", user);

        String payload = invitationEvent.getMessage().getPayload();
        System.out.println(payload);

        InvitationEvent.InvitationMessagePayload deserialize = InvitationEvent.InvitationMessagePayload.deserialize(payload, InvitationEvent.InvitationMessagePayload.class);
        System.out.println(deserialize);

    }
}
