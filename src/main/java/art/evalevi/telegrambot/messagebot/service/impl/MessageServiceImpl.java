package art.evalevi.telegrambot.messagebot.service.impl;

import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import art.evalevi.telegrambot.messagebot.repository.MessageRepository;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ChatMessage} interface.
 */
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Override
    public Optional<ChatMessage> findByChatIdAndType(Long chatId, MessageType type) {
        return messageRepository.findByChatIdAndType(chatId, type);
    }

    @Override
    public Optional<ChatMessage> findByChatIdAndTypeAndStatus(Long chatId, MessageType type, MessageStatus status) {
        return messageRepository.findByChatIdAndTypeAndStatus(chatId, type, status);
    }

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        messageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findAllByScheduledTime(LocalDateTime time) {
        return messageRepository.findAllByScheduledTime(time);
    }
}
