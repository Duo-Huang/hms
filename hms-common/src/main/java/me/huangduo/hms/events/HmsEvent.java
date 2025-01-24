package me.huangduo.hms.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.dto.model.Message;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Getter
public abstract class HmsEvent<T extends HmsEvent.MessagePayload> extends ApplicationEvent {

    private final Message<T> message;

    public HmsEvent(Object source, Message<T> message) {
        super(source);
        this.message = message;
    }

    // 子类必须继续
    @Data
    @AllArgsConstructor
    @SuperBuilder
    public static abstract class MessagePayload implements Serializable {
        private final Integer publisherUserId; // who trigger this event, userId
    }
}
