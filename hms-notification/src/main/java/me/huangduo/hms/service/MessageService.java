package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Message;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessageService {
    Flux<Message> getLiveMessage(Integer homeId);

    List<Message> getHistoryMessages(Integer homeId); // TODO: pageable

}
