package art.evalevi.telegrambot.messagebot.config;

import art.evalevi.telegrambot.messagebot.bot.MessageBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * The class that describes the bot configuration
 */
@Configuration
public class MessageBotConfig {

    private final MessageBot messageBot;

    @Autowired
    public MessageBotConfig(MessageBot messageBot) {
        this.messageBot = messageBot;
    }

    /**
     * Method that registers a bot in the Telegram API
     * @return TelegramBotsApi object
     * @throws TelegramApiException if the bot is not registered
     */
    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(messageBot);
        return botsApi;
    }
}