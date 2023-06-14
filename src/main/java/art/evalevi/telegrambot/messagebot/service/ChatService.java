package art.evalevi.telegrambot.messagebot.service;

import art.evalevi.telegrambot.messagebot.model.Chat;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link Chat} entity.
 */
public interface ChatService {
    List<Chat> findAll();

    void save(Chat chat);

    void deleteById(Long chatId);

    Optional<Chat> findByChatId(Long chatId);
}
