package art.evalevi.telegrambot.messagebot.service.impl;

import art.evalevi.telegrambot.messagebot.event.SendMessageEvent;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link SendMessageService} interface.
 */
@Service
@AllArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void sendMessage(Long chatId, String text) {
        eventPublisher.publishEvent(new SendMessageEvent(this, chatId, text));
    }
}
