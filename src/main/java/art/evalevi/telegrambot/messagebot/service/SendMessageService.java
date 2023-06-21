package art.evalevi.telegrambot.messagebot.service;

/**
 * Service for sending messages.
 */
public interface SendMessageService {
    void sendMessage(Long chatId, String text);
}
