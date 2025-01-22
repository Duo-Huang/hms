package me.huangduo.hms.service;

import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.HmsEvent;

public interface EventHandlerService {
    void registerHandler(Class<? extends HmsEvent> eventType, EventHandler handler);

    void handleEvent(HmsEvent event);
}
