package art.evalevi.telegrambot.messagebot.repository;

import art.evalevi.telegrambot.messagebot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Chat} entity.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}

