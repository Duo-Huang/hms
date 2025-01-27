package me.huangduo.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.utils.JsonUtil;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Getter
public abstract class HmsEvent extends ApplicationEvent {

    private final Message message;

    public HmsEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    // 子类必须继续
    @Data
    @AllArgsConstructor
    @SuperBuilder
    public static abstract class MessagePayload implements Serializable {
        private final Integer publisherUserId; // who trigger this event, userId

        public static <T extends MessagePayload> T deserialize(String payload, Class<T> clazz) {
            return JsonUtil.deserialize(payload, clazz);
        }

        public String serialize() {
            return JsonUtil.serialize(this);
        }
    }
}
