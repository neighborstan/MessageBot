package art.evalevi.telegrambot.messagebot.service;

import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for {@link ChatMessage} entity.
 */
public interface MessageService {
    Optional<ChatMessage> findByChatIdAndType(Long chatId, MessageType type);

    Optional<ChatMessage> findByChatIdAndTypeAndStatus(Long chatId, MessageType type, MessageStatus status);

    void saveMessage(ChatMessage chatMessage);

    List<ChatMessage> findAllByScheduledTime(LocalDateTime time);
}

