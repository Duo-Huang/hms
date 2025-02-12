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

//        EventHandler genericHandler = event -> this.sinksManager.emit(event.getMessage());

        // register event handler
        eventHandlerService.registerEventHandler(InvitationEvent.class, evt -> {
            InvitationEvent.InvitationMessagePayload messagePayload = (InvitationEvent.InvitationMessagePayload) evt.getMessage().getDeserializedPayload();
            sinksManager.sendDirectMessage(messagePayload.getReceiver().getUserId(), evt.getMessage());
        });
        eventHandlerService.registerEventHandler(NotificationEvent.class, evt -> {
            NotificationEvent.NotificationMessagePayload messagePayload = (NotificationEvent.NotificationMessagePayload) evt.getMessage().getDeserializedPayload();
            sinksManager.sendHomeMessage(messagePayload.getHomeId(), evt.getMessage());
        });

        eventHandlerService.registerEventHandler(BroadcastEvent.class, evt -> sinksManager.sendBroadcastMessage(evt.getMessage()));
    }

    @Override
    public Flux<Message> getLiveMessage(User userInfo) {

        return sinksManager.getFlux(userInfo.getUserId())
                .doOnCancel(() -> {
                    log.info("doOnCancel - remove skin: {}", userInfo.getUserId());
                    sinksManager.removeSkin(userInfo.getUserId());
                })
                .doOnComplete(() -> {
                    log.info("doOnComplete - remove skin: {}", userInfo.getUserId());
                    sinksManager.removeSkin(userInfo.getUserId());
                })
                .doOnTerminate(() -> {
                    log.info("doOnTerminate - remove skin: {}", userInfo.getUserId());
                    sinksManager.removeSkin(userInfo.getUserId());
                }).map(message -> {
                    if (message.getMessageType() == MessageTypeEnum.INVITATION) {
                        HmsEvent.MessagePayload messagePayload = message.getDeserializedPayload();
                        if (messagePayload instanceof InvitationEvent.InvitationMessagePayload invitationMessagePayload) {
                            if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getReceiver().getUserId())) {
                                message.setMessageContent(String.format("%s 邀请你加入他的家庭", invitationMessagePayload.getPublisher().getNickname()));
                            }

                            if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getPublisher().getUserId())) {
                                message.setMessageContent(String.format("你已邀请 %s 加入家庭", invitationMessagePayload.getReceiver().getNickname()));
                            }
                        }

                    }
                    return message;
                });
    }

    @Override
    public List<Message> getHistoryMessages(User userInfo, Integer homeId) {
        // directed message & home message & broadcast message

        List<MessageEntity> directedMessage = messagesDao.getHistoryMessagesByReceiveUserId(userInfo.getUserId());
        List<MessageEntity> restMessages;

        if (homeId == 0) {
            restMessages = messagesDao.getHistoryMessagesByType(MessageTypeEnum.BROADCAST);
        } else {
            restMessages = messagesDao.getHistoryMessagesByTypeOrHomeId(MessageTypeEnum.BROADCAST, homeId);
        }


        return Stream.concat(directedMessage.stream(), restMessages.stream()).map(x -> {
            Message message = messageMapper.toModel(x);

            if (message.getMessageType() == MessageTypeEnum.INVITATION) {
                HmsEvent.MessagePayload messagePayload = message.getDeserializedPayload();
                if (messagePayload instanceof InvitationEvent.InvitationMessagePayload invitationMessagePayload) {
                    if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getReceiver().getUserId())) {
                        message.setMessageContent(String.format("%s 邀请你加入他的家庭", invitationMessagePayload.getPublisher().getNickname()));
                    }

                    if (Objects.equals(userInfo.getUserId(), invitationMessagePayload.getPublisher().getUserId())) {
                        message.setMessageContent(String.format("你已邀请 %s 加入家庭", invitationMessagePayload.getReceiver().getNickname()));
                    }
                }

            }
            return message;
        }).sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())).collect(Collectors.toList());
    }

}
