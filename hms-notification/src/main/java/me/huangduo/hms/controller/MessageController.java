package me.huangduo.hms.controller;

import jakarta.servlet.http.HttpServletResponse;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.MessageResponse;
import me.huangduo.hms.mapper.MessageMapper;
import me.huangduo.hms.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    private final MessageMapper messageMapper;

    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @GetMapping
    public ResponseEntity<HmsResponseBody<List<MessageResponse>>> getHistoryMessages(@RequestAttribute Integer homeId) {
        return ResponseEntity.ok(HmsResponseBody.success(messageService.getHistoryMessages(homeId).stream().map(messageMapper::toResponse).collect(Collectors.toList())));
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageResponse> streamLiveMessages(@RequestAttribute Integer homeId, HttpServletResponse response) {
//        response.setHeader("Content-Security-Policy", "default-src 'self'; connect-src 'self' http://localhost:8081;");
        return messageService.getLiveMessage(homeId).map(messageMapper::toResponse);
    }

}
