package art.evalevi.telegrambot.messagebot.config;

import art.evalevi.telegrambot.messagebot.bot.MessageBot;
import art.evalevi.telegrambot.messagebot.bot.MessageBotCommand;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.List;

/**
 * The class that describes the bot configuration
 */
@Configuration
public class MessageBotConfig {

    List<BotCommand> menuCommands = Arrays.asList(
            new BotCommand(MessageBotCommand.SHOW_INFO.getCommand(), "Description and features"),
            new BotCommand(MessageBotCommand.SHOW_ALL_GROUPS.getCommand(), "List of bot groups")
    );

    private final MessageBot messageBot;

    @Autowired
    public MessageBotConfig(MessageBot messageBot) {
        this.messageBot = messageBot;
    }

    /**
     * Method that registers bot commands from menu
     * @throws TelegramApiException if the command is not registered
     */
    @PostConstruct
    public void registerCommands() throws TelegramApiException {
        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(menuCommands);
        messageBot.execute(setMyCommands);
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