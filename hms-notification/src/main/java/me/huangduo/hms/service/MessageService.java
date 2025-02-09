package me.huangduo.hms.service;

import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.User;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessageService {
    Flux<Message> getLiveMessage(User userInfo, Integer homeId);

    List<Message> getHistoryMessages(User userInfo, Integer homeId); // TODO: pageable

}
