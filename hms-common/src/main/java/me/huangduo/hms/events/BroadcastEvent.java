package me.huangduo.hms.events;

import me.huangduo.hms.dto.model.Message;

public class BroadcastEvent extends HmsEvent {
    public BroadcastEvent(Object source, Message message) {
        super(source, message);
    }
}
