package art.evalevi.telegrambot.messagebot.service.impl;

import art.evalevi.telegrambot.messagebot.model.Chat;
import art.evalevi.telegrambot.messagebot.repository.ChatRepository;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ChatService} interface.
 */
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public void save(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    public void deleteById(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    @Override
    public Optional<Chat> findByChatId(Long chatId) {
        return chatRepository.findById(chatId);
    }
}
