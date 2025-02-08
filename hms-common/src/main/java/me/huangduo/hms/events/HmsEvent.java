package me.huangduo.hms.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    /*
     * 子类必须继承, 实现event的message payload
     * */
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


    /*
     * 子类必须继承, 实现event的message payload response
     * */
    @Data
    @AllArgsConstructor
    @SuperBuilder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static abstract class MessagePayloadResponse implements Serializable {

    }
}
