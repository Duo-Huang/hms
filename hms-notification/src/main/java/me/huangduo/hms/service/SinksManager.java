package me.huangduo.hms.service;

import lombok.Getter;
import me.huangduo.hms.dto.model.Message;
import reactor.core.publisher.Sinks;

@Getter
public class SinksManager {

    private static SinksManager instance;

    private final Sinks.Many<Message> sink;

    private SinksManager() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public static synchronized SinksManager getInstance() {
        if (instance == null) {
            instance = new SinksManager();
        }
        return instance;
    }

    public void emit(Message event) {
        sink.tryEmitNext(event);
    }
}
