package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.config.AppConfig;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.model.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SinksManager {

    private final AppConfig appConfig;
    private final CommonService commonService;
    private final Map<Integer, Sinks.Many<Message>> userSinks = new ConcurrentHashMap<>();

    public SinksManager(AppConfig appConfig, CommonService commonService) {
        this.appConfig = appConfig;
        this.commonService = commonService;
    }

    private Sinks.Many<Message> registerSink(Integer userId) {
        userSinks.computeIfAbsent(userId, k -> Sinks.many().multicast().onBackpressureBuffer());
        return userSinks.get(userId);
    }

    private void removeSink(Integer userId) {
        Sinks.Many<Message> sink = userSinks.get(userId);
        if (Objects.nonNull(sink)) {
            log.debug("[Before remove sink] The sink for this user {} have {} subscribers", userId, sink.currentSubscriberCount());
            if (sink.currentSubscriberCount() == 0) {
                userSinks.remove(userId);
                log.debug("Sink has been removed for user {}", userId);
            }
        }
    }

    public void startHeartbeat() {
        Flux.interval(Duration.ofSeconds(appConfig.getSseHeartbeatIntervalOfSec()))
                .doOnSubscribe(subscription -> log.info("SSE heartbeat started"))
                .subscribe(tick -> sendHeartbeatMessage());
    }

    private void sendHeartbeatMessage() {
        sendBroadcastMessage(new Message(0, MessageTypeEnum.HEARTBEAT, null, null, null, null));
    }

    public void sendBroadcastMessage(Message message) {
        if (userSinks.isEmpty()) {
            log.info("No any sinks found");
            return;
        }

        log.info("Message will send to {} users", userSinks.size());

        for (Map.Entry<Integer, Sinks.Many<Message>> entry : userSinks.entrySet()) {
            Integer userId = entry.getKey();
            Sinks.Many<Message> sink = entry.getValue();
            log.debug("The broadcast message will send to {} subscribers for this user {}", sink.currentSubscriberCount(), userId);
            sendMessage(sink, message, userId);
        }
    }

    public void sendDirectMessage(Integer userId, Message message) {
        Sinks.Many<Message> sink = userSinks.get(userId);
        if (Objects.isNull(sink)) {
            log.info("No sink found for userId: " + userId);
        } else {
            log.debug("The Direct message will send to {} subscribers for this user {}", sink.currentSubscriberCount(), userId);
            sendMessage(sink, message, userId);
        }
    }

    public void sendHomeMessage(Integer homeId, Message message) {
        commonService.getHomeMemberUserIds(homeId).forEach(x -> {
            sendDirectMessage(x, message);
        });
    }

    public Flux<Message> getFlux(Integer userId) {

        return Flux.using(() -> registerSink(userId), Sinks.Many::asFlux, (userSink) -> {
            log.debug("Flux using clean");
            removeSink(userId);

        }).doOnSubscribe(subscription -> {
            log.debug("doOnSubscribe - A new subscriber for user: {}", userId);
        });
    }

    private void sendMessage(Sinks.Many<Message> sink, Message message, Integer userId) {
        if (sink.currentSubscriberCount() == 0) {
            log.info("No subscribers for the sink. Message dropped: " + message);
            return;
        }

        try {
            log.debug("The sink for user {} is sending message", userId);
            Sinks.EmitResult result = sink.tryEmitNext(message);
            if (result.isFailure()) {
                handleEmitFailure(result, message);
            }
        } catch (Exception e) {
            log.error("send failed", e);
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
