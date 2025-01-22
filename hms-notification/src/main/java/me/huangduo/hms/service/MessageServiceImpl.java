package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final Sinks.Many<Message> sink = SinksManager.getInstance().getSink();

    public MessageServiceImpl(EventHandlerService eventHandlerService) {
        EventHandler genericHandler = new EventHandler() {
            @Override
            public void handle(HmsEvent event) {
                Message message = event.getMessage();
                Sinks.EmitResult result = sink.tryEmitNext(message);
                if (result.isFailure()) {
                    switch (result) {
                        case FAIL_TERMINATED:
                            System.err.println("Sink has been terminated. Cannot send message: " + message);
                            break;
                        case FAIL_OVERFLOW:
                            System.err.println("Sink buffer is full. Message dropped: " + message);
                            break;
                        default:
                            System.err.println("Unknown error occurred while sending message: " + message);
                    }
                } else {
                    System.out.println("Message sent successfully: " + message);
                }
            }
        };


        eventHandlerService.registerHandler(InvitationEvent.class, genericHandler);
    }

    @Override
    public Flux<Message> getLiveMessage() {
        return SinksManager.getInstance().getSink().asFlux();
    }

    @Override
    public List<Message> getHistoryMessages(Integer homeId) {
        return null;
    }
}
