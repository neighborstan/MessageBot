package art.evalevi.telegrambot.messagebot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Chat DTO.
 */
@Data
@AllArgsConstructor
public class ChatDto {
    private Long chatId;
    private String name;
}
