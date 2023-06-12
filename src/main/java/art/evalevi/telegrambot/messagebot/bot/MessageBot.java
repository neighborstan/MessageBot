package art.evalevi.telegrambot.messagebot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * The class that describes the bot
 */
@Component
public class MessageBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    /**
     * Constructor that accepts bot settings and a token
     * @param token bot token
     */
    public MessageBot(@Value("${telegram.bot.token}") String token) {
        super(new DefaultBotOptions(), token);
    }

    /**
     * The method that will be called when a message is received from the user
     * @param update an object containing information about the message
     */
    @Override
    public void onUpdateReceived(Update update) {

    }

    /**
     * Method that returns the name of the bot specified during registration
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return username;
    }
}
