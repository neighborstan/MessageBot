package art.evalevi.telegrambot.messagebot.command;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Interface for command pattern
 */
public interface Command {
    void execute(Message message);
}
