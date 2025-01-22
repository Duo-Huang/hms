package me.huangduo.hms.events;

import lombok.Getter;
import me.huangduo.hms.dto.model.Message;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class HmsEvent extends ApplicationEvent {

    private final Message message;

    public HmsEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    // 子类需要时继承实现即可
    public static class MessagePayload {

    }
}
