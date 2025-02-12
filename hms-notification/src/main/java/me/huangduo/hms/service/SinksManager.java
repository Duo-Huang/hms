package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.model.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SinksManager {

    private final CommonService commonService;
    private final Map<Integer, Sinks.Many<Message>> userSinks = new ConcurrentHashMap<>();

    public SinksManager(CommonService commonService) {
        this.commonService = commonService;
    }

    public void registerSkin(Integer userId) {
        userSinks.put(userId, Sinks.many().multicast().onBackpressureBuffer());
    }

    public void removeSkin(Integer userId) {
        userSinks.remove(userId);
    }

    public void sendBroadcastMessage(Message message) {
        for (Sinks.Many<Message> sink : userSinks.values()) {
            Sinks.EmitResult result = sink.tryEmitNext(message);
            if (result.isFailure()) {
                handleEmitFailure(result, message);
            } else {
                System.out.println("Message sent successfully: " + message);
            }
        }
    }

    public void sendDirectMessage(Integer userId, Message message) {
        Sinks.Many<Message> sink = userSinks.get(userId);
        if (sink != null) {
            Sinks.EmitResult result = sink.tryEmitNext(message);
            if (result.isFailure()) {
                handleEmitFailure(result, message);
            } else {
                System.out.println("Message sent successfully: " + message);
            }
        }
    }

    public void sendHomeMessage(Integer homeId, Message message) {
        commonService.getHomeMemberUserIds(homeId).forEach(x -> {
            sendDirectMessage(x, message);
        });
    }

    public Flux<Message> getFlux(Integer userId) {
        return userSinks.get(userId).asFlux();
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
