package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.HmsEvent;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessageService {
    Flux<Message<? extends HmsEvent.MessagePayload>> getLiveMessage();

    List<Message<? extends HmsEvent.MessagePayload>> getHistoryMessages(Integer homeId);

}
