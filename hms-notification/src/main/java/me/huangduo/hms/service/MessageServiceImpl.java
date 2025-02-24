package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.MessagesDao;
import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.events.BroadcastEvent;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.mapper.MessageMapper;
import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final EventHandlerService eventHandlerService;
    private final SinksManager sinksManager;
    private final MessagesDao messagesDao;
    private final MessageMapper messageMapper;

    private final TemplateRenderService templateRenderService;

    public MessageServiceImpl(EventHandlerService eventHandlerService, SinksManager sinksManager, MessagesDao messagesDao, MessageMapper messageMapper, TemplateRenderService templateRenderService) {
        this.eventHandlerService = eventHandlerService;
        this.sinksManager = sinksManager;
        this.messagesDao = messagesDao;
        this.messageMapper = messageMapper;
        this.templateRenderService = templateRenderService;
    }

    @Override
    public void run(String... args) throws Exception {
        registerEventHandlers(eventHandlerService);
        sinksManager.startHeartbeat();
    }

    private void registerEventHandlers(EventHandlerService eventHandlerService) {
        eventHandlerService.registerEventHandler(InvitationEvent.class, this::handleInvitationEvent);
        eventHandlerService.registerEventHandler(NotificationEvent.class, this::handleNotificationEvent);
        eventHandlerService.registerEventHandler(BroadcastEvent.class, evt -> sinksManager.sendBroadcastMessage(evt.getMessage()));
    }

    private void handleInvitationEvent(HmsEvent evt) {
        InvitationEvent.InvitationMessagePayload messagePayload =
                (InvitationEvent.InvitationMessagePayload) evt.getMessage().getDeserializedPayload();
        sinksManager.sendDirectMessage(messagePayload.getReceiver().getUserId(), evt.getMessage());
    }

    private void handleNotificationEvent(HmsEvent evt) {
        NotificationEvent.NotificationMessagePayload messagePayload =
                (NotificationEvent.NotificationMessagePayload) evt.getMessage().getDeserializedPayload();
        sinksManager.sendHomeMessage(messagePayload.getHomeId(), evt.getMessage());
    }

    @Override
    public Flux<Message> getLiveMessage(User userInfo) {
        return sinksManager.getFlux(userInfo.getUserId()).map(message -> convertMessage(userInfo, message));
    }

    @Override
    public List<Message> getHistoryMessages(User userInfo, Integer homeId) {
        // directed message & home message & broadcast message
        List<MessageEntity> directedMessages = messagesDao.getHistoryMessagesByReceiveUserId(userInfo.getUserId());
        List<MessageEntity> otherMessages = getOtherMessages(homeId);

        return Stream.concat(directedMessages.stream(), otherMessages.stream())
                .map(x -> mapToModelWithConvert(userInfo, x))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    private Message convertMessage(User userInfo, Message message) {
        if (message.getMessageType() != MessageTypeEnum.HEARTBEAT) {
            message.setMessageContent(templateRenderService.render(message.getMessageContent(), userInfo));
        }
        return message;
    }

    private List<MessageEntity> getOtherMessages(Integer homeId) {
        return homeId == 0
                ? messagesDao.getHistoryMessagesByType(MessageTypeEnum.BROADCAST)
                : messagesDao.getHistoryMessagesByTypeOrHomeId(MessageTypeEnum.BROADCAST, homeId);
    }

    private Message mapToModelWithConvert(User userInfo, MessageEntity entity) {
        Message message = messageMapper.toModel(entity);
        return convertMessage(userInfo, message);
    }
}