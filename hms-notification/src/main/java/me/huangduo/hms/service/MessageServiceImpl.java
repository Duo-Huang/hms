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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final SinksManager sinksManager;
    private final MessagesDao messagesDao;
    private final MessageMapper messageMapper;

    public MessageServiceImpl(EventHandlerService eventHandlerService, SinksManager sinksManager, MessagesDao messagesDao, MessageMapper messageMapper) {
        this.sinksManager = sinksManager;
        this.messagesDao = messagesDao;
        this.messageMapper = messageMapper;

        registerEventHandlers(eventHandlerService);
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
        return sinksManager.getFlux(userInfo.getUserId())
                .doOnCancel(() -> cleanUpUser(userInfo))
                .doOnComplete(() -> cleanUpUser(userInfo))
                .doOnTerminate(() -> cleanUpUser(userInfo))
                .map(message -> enrichMessage(userInfo, message));
    }

    private void cleanUpUser(User userInfo) {
        log.info("Cleanup - remove skin: {}", userInfo.getUserId());
        sinksManager.removeSkin(userInfo.getUserId());
    }

    private Message enrichMessage(User userInfo, Message message) {
        if (message.getMessageType() == MessageTypeEnum.INVITATION) {
            Optional<InvitationEvent.InvitationMessagePayload> payload =
                    extractInvitationPayload(message);
            payload.ifPresent(invitationMessagePayload -> updateMessageContent(userInfo, message, invitationMessagePayload));
        }
        return message;
    }

    private Optional<InvitationEvent.InvitationMessagePayload> extractInvitationPayload(Message message) {
        HmsEvent.MessagePayload messagePayload = message.getDeserializedPayload();
        return messagePayload instanceof InvitationEvent.InvitationMessagePayload
                ? Optional.of((InvitationEvent.InvitationMessagePayload) messagePayload)
                : Optional.empty();
    }

    private void updateMessageContent(User userInfo, Message message, InvitationEvent.InvitationMessagePayload invitationMessagePayload) {
        if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getReceiver().getUserId())) {
            message.setMessageContent(String.format("%s 邀请你加入他的家庭", invitationMessagePayload.getPublisher().getNickname()));
        } else if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getPublisher().getUserId())) {
            message.setMessageContent(String.format("你已邀请 %s 加入家庭", invitationMessagePayload.getReceiver().getNickname()));
        }
    }

    @Override
    public List<Message> getHistoryMessages(User userInfo, Integer homeId) {
        // directed message & home message & broadcast message
        List<MessageEntity> directedMessages = messagesDao.getHistoryMessagesByReceiveUserId(userInfo.getUserId());
        List<MessageEntity> otherMessages = getOtherMessages(homeId);

        return Stream.concat(directedMessages.stream(), otherMessages.stream())
                .map(x -> mapToModelWithEnrichment(userInfo, x))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    private List<MessageEntity> getOtherMessages(Integer homeId) {
        return homeId == 0
                ? messagesDao.getHistoryMessagesByType(MessageTypeEnum.BROADCAST)
                : messagesDao.getHistoryMessagesByTypeOrHomeId(MessageTypeEnum.BROADCAST, homeId);
    }

    private Message mapToModelWithEnrichment(User userInfo, MessageEntity entity) {
        Message message = messageMapper.toModel(entity);
        return enrichMessage(userInfo, message);
    }
}