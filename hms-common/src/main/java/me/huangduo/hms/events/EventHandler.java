package me.huangduo.hms.events;

@FunctionalInterface
public interface EventHandler {
    void handle(HmsEvent event);
}
