package art.evalevi.telegrambot.messagebot.repository;

import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link ChatMessage} entity.
 */
@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findByChatIdAndType(Long chatId, MessageType type);

    Optional<ChatMessage> findByChatIdAndTypeAndStatus(Long chatId, MessageType type, MessageStatus status);

    List<ChatMessage> findAllByScheduledTime(LocalDateTime time);
}
