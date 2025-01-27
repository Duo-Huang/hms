package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

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
                System.err.println("Sink has been terminated. Cannot send message: " + message);
                break;
            case FAIL_OVERFLOW:
                System.err.println("Sink buffer is full. Message dropped: " + message);
                break;
            case FAIL_CANCELLED:
                System.err.println("Sink has been cancelled. Cannot send message: " + message);
                break;
            case FAIL_NON_SERIALIZED:
                System.err.println("Message could not be serialized. Message: " + message);
                break;
            case FAIL_ZERO_SUBSCRIBER:
                System.err.println("No subscribers for the sink. Message dropped: " + message);
                break;
            default:
                System.err.println("Unknown error occurred while sending message: " + message);
        }
    }
}
