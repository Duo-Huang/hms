package me.huangduo.hms.service;

import me.huangduo.hms.dao.MessagesDao;
import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.events.EventHandler;
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
public class MessageServiceImpl implements MessageService {

    private final SinksManager sinksManager = SinksManager.getInstance();

    private final MessagesDao messagesDao;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(EventHandlerService eventHandlerService, MessagesDao messagesDao, MessageMapper messageMapper) {
        this.messagesDao = messagesDao;
        this.messageMapper = messageMapper;

        EventHandler genericHandler = event -> sinksManager.emit(event.getMessage());

        // register event handler
        eventHandlerService.registerEventHandler(InvitationEvent.class, genericHandler);
        eventHandlerService.registerEventHandler(NotificationEvent.class, genericHandler);
    }

    @Override
    public Flux<Message> getLiveMessage(User userInfo, Integer homeId) {

        return sinksManager.getFlux().doOnCancel(() -> {

        });
    }

    @Override
    public List<Message> getHistoryMessages(User userInfo, Integer homeId) {
        // directed message & home message & broadcast message

        List<MessageEntity> directedMessage = messagesDao.getHistoryMessagesByReceiveUserId(userInfo.getUserId());
        List<MessageEntity> homeMessages = messagesDao.getHistoryMessagesByTypeOrHomeId(MessageTypeEnum.BROADCAST, homeId);

        return Stream.concat(directedMessage.stream(), homeMessages.stream()).map(x -> {
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

    private boolean currentUserCanReceive(Message message, User user, Integer homeId) {
        HmsEvent.MessagePayload messagePayload = message.getDeserializedPayload();

        if (message.getMessageType() == MessageTypeEnum.INVITATION) {
            if (messagePayload instanceof InvitationEvent.InvitationMessagePayload invitationMessagePayload) {
                if (Objects.equals(user.getUserId(), invitationMessagePayload.getReceiver().getUserId())) {
                    return true;
                }
            }

        }
        return false;
    }

}
