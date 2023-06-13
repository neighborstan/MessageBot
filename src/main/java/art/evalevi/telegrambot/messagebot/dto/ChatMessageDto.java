package art.evalevi.telegrambot.messagebot.dto;

import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Chat message DTO.
 */
@Data
@AllArgsConstructor
public class ChatMessageDto {
    private Long chatId;
    private MessageType type;
    private String text;
    private LocalDateTime scheduledTime;
    private MessageStatus status;
}
