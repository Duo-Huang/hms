package me.huangduo.hms.controller;

import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.MessageResponse;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.mapper.MessageMapper;
import me.huangduo.hms.model.User;
import me.huangduo.hms.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
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
    public ResponseEntity<HmsResponseBody<List<MessageResponse>>> getHistoryMessages(@RequestAttribute User userInfo, @RequestAttribute Integer homeId) {
        return ResponseEntity.ok(HmsResponseBody.success(messageService.getHistoryMessages(userInfo, homeId).stream().map(messageMapper::toResponse).collect(Collectors.toList())));
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<MessageResponse>> streamLiveMessages(@RequestAttribute User userInfo) {
        return messageService.getLiveMessage(userInfo).map(x -> {
            MessageResponse msg = messageMapper.toResponse(x);
            if (msg.messageType() == MessageTypeEnum.HEARTBEAT) {
                return ServerSentEvent.builder((MessageResponse) null).id(msg.messageId().toString()).event(msg.messageType().name()).build();
            }
            return ServerSentEvent.builder(msg).id(msg.messageId().toString()).event(msg.messageType().name()).build();
        });
    }

}
