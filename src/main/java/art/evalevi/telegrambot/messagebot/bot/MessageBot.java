package art.evalevi.telegrambot.messagebot.bot;

import art.evalevi.telegrambot.messagebot.command.*;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;

/**
 * The class that describes the bot
 */
@Component
public class MessageBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    private final ChatService chatService;
    private final MessageService messageService;
    private final SendMessageService sendMessageService;

    private final EnumMap<MessageBotCommand, Command> commandMap = new EnumMap<>(MessageBotCommand.class);

    public MessageBot(MessageService messageService,
                      ChatService chatService,
                      SendMessageService sendMessageService,
                      @Value("${telegram.bot.token}") String token) {

        super(new DefaultBotOptions(), token);

        this.chatService = chatService;
        this.messageService = messageService;
        this.sendMessageService = sendMessageService;

        commandMap.put(MessageBotCommand.CHANGE_WELCOME, new ChangeWelcomeCommand(messageService, sendMessageService, chatService));
        commandMap.put(MessageBotCommand.CHANGE_TIMED_MESSAGE, new ChangeTimedMessageCommand(messageService, sendMessageService, chatService));
        commandMap.put(MessageBotCommand.CHANGE_SCHEDULED_TIME, new ChangeScheduledTimeCommand(messageService, sendMessageService, chatService));
        commandMap.put(MessageBotCommand.SHOW_ALL_GROUPS, new ShowAllGroupsCommand(chatService, sendMessageService));
        commandMap.put(MessageBotCommand.SHOW_INFO, new ShowInfoCommand(sendMessageService));
    }

    /**
     * The method that will be called when a message is received from the user
     *
     * @param update an object containing information about the message
     */
    @Override
    public void onUpdateReceived(Update update) {

    }

    /**
     * Method that returns the name of the bot specified during registration
     *
     * @return bot name
     */
    @Override
    public String getBotUsername() {
        return username;
    }
}
