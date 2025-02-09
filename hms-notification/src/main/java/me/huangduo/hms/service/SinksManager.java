package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class SinksManager {

    private static volatile SinksManager instance;
    private final Sinks.Many<Message> sink;

    private SinksManager() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public static SinksManager getInstance() {
        SinksManager localInstance = instance;
        if (localInstance == null) {
            synchronized (SinksManager.class) {
                if (instance == null) {
                    instance = new SinksManager();
                }
            }
        }
        return instance;
    }

    public Flux<Message> getFlux() {
        return sink.asFlux();
    }

    public void emit(Message message) {
        Sinks.EmitResult result = sink.tryEmitNext(message);
        if (result.isFailure()) {
            handleEmitFailure(result, message);
        } else {
            System.out.println("Message sent successfully: " + message);
        }
    }

    private void handleEmitFailure(Sinks.EmitResult result, Message message) {
        switch (result) {
            case FAIL_TERMINATED:
                log.error("Sink has been terminated. Cannot send message: " + message);
                break;
            case FAIL_OVERFLOW:
                log.error("Sink buffer is full. Message dropped: " + message);
                break;
            case FAIL_CANCELLED:
                log.error("Sink has been cancelled. Cannot send message: " + message);
                break;
            case FAIL_NON_SERIALIZED:
                log.error("Message could not be serialized. Message: " + message);
                break;
            case FAIL_ZERO_SUBSCRIBER:
                log.error("No subscribers for the sink. Message dropped: " + message);
                break;
            default:
                log.error("Unknown error occurred while sending message: " + message);
        }
    }
}
