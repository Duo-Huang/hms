package me.huangduo.hms.controller;

import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<HmsResponseBody<Void>> getHistoryMessages() {
        return ResponseEntity.ok(HmsResponseBody.success());
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamLiveMessages() {
        return messageService.getLiveMessage();
    }

}
