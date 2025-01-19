package me.huangduo.hms.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class MessageController {

    @GetMapping(value = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() {
        // 每隔 1 秒发送一个事件
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Event " + sequence);
    }
}
