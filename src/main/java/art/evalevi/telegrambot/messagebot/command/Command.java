package art.evalevi.telegrambot.messagebot.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {
    void execute(Message message);
}
