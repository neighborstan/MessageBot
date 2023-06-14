package art.evalevi.telegrambot.messagebot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ChatMessage entity.
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;

    @Column(name = "text", nullable = false, columnDefinition="TEXT")
    private String text;

    @Column(name = "scheduled_time", columnDefinition= "TIMESTAMP(0)")
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status;
}
