package me.huangduo.hms.controller;

import jakarta.servlet.http.HttpServletResponse;
import me.huangduo.hms.dto.response.HmsResponseBody;
import me.huangduo.hms.dto.response.MessageResponse;
import me.huangduo.hms.mapper.MessageMapper;
import me.huangduo.hms.model.User;
import me.huangduo.hms.service.MessageService;
import me.huangduo.hms.service.SinksManager;
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

    private final SinksManager sinksManager;

    public MessageController(MessageService messageService, MessageMapper messageMapper, SinksManager sinksManager) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
        this.sinksManager = sinksManager;
    }

    @GetMapping
    public ResponseEntity<HmsResponseBody<List<MessageResponse>>> getHistoryMessages(@RequestAttribute User userInfo, @RequestAttribute Integer homeId) {
        return ResponseEntity.ok(HmsResponseBody.success(messageService.getHistoryMessages(userInfo, homeId).stream().map(messageMapper::toResponse).collect(Collectors.toList())));
    }

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageResponse> streamLiveMessages(@RequestAttribute User userInfo, HttpServletResponse response) {
        sinksManager.registerSkin(userInfo.getUserId());
//        response.setHeader("Content-Security-Policy", "default-src 'self'; connect-src 'self' http://localhost:8081;");
        return messageService.getLiveMessage(userInfo).map(messageMapper::toResponse);
    }

}
