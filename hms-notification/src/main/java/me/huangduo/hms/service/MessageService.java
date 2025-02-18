package me.huangduo.hms.service;

import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.User;
import org.springframework.boot.CommandLineRunner;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MessageService extends CommandLineRunner {
    Flux<Message> getLiveMessage(User userInfo);

    List<Message> getHistoryMessages(User userInfo, Integer homeId); // TODO: pageable

}
