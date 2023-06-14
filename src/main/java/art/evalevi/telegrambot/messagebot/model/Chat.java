package art.evalevi.telegrambot.messagebot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat entity.
 */
@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "name", nullable = false)
    private String name;
}
