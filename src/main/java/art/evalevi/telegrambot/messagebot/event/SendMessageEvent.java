package art.evalevi.telegrambot.messagebot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Send message event
 */
@Getter
public class SendMessageEvent extends ApplicationEvent {
    private final Long chatId;
    private final String text;

    public SendMessageEvent(Object source, Long chatId, String text) {
        super(source);
        this.chatId = chatId;
        this.text = text;
    }
}
