package art.evalevi.telegrambot.messagebot.bot;

import art.evalevi.telegrambot.messagebot.command.*;
import art.evalevi.telegrambot.messagebot.dto.ChatDto;
import art.evalevi.telegrambot.messagebot.dto.ChatMessageDto;
import art.evalevi.telegrambot.messagebot.event.SendMessageEvent;
import art.evalevi.telegrambot.messagebot.mapper.ChatMapper;
import art.evalevi.telegrambot.messagebot.mapper.ChatMessageMapper;
import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        if (isBotRemovedFromChat(update)) {
            handleBotRemovedFromChat(update);
        }

        if (isBotAddedToChat(update)) {
            handleNewChatMembers(update);
        }

        if (hasTextMessage(update)) {
            handleTextMessage(update);
        }
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

    /**
     * Event listener for the {@link SendMessageEvent}
     *
     * @param event the event object that is substituted when it is called
     */
    @EventListener
    public void handleSendMessageEvent(SendMessageEvent event) {
        sendBotMessage(event.getChatId(), event.getText());
    }

    private void sendBotMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.enableHtml(true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            // TODO handle an error
        }
    }

    private boolean isBotRemovedFromChat(Update update) {
        return update.hasMessage() &&
                update.getMessage().getLeftChatMember() != null &&
                update.getMessage().getLeftChatMember().getUserName().equals(getBotUsername());
    }

    private boolean isBotAddedToChat(Update update) {
        return update.hasMessage() && update.getMessage().getNewChatMembers() != null;
    }

    private void handleBotRemovedFromChat(Update update) {
        Long chatId = update.getMessage().getChatId();
        chatService.deleteById(chatId);
    }

    private void handleNewChatMembers(Update update) {
        for (User newUser : update.getMessage().getNewChatMembers()) {
            if (newUser.getUserName().equals(getBotUsername())) {
                saveNewChat(update);
            } else {
                sendWelcomeMessageIfNewUserAdded(update);
            }
        }
    }

    private void saveNewChat(Update update) {
        long chatId = update.getMessage().getChatId();
        String chatName = update.getMessage().getChat().getTitle();
        chatService.save(ChatMapper.INSTANCE.chatDtoToChat(new ChatDto(chatId, chatName)));
    }

    private void sendWelcomeMessageIfNewUserAdded(Update update) {
        ChatMessage welcomeChatMessage = messageService.findByChatIdAndType(update.getMessage().getChatId(), MessageType.WELCOME)
                .orElse(ChatMessageMapper.INSTANCE.messageDtoToMessage(
                        new ChatMessageDto(
                                update.getMessage().getChatId(), MessageType.WELCOME, "Привет!", null, null)));

        sendBotMessage(update.getMessage().getChatId(), welcomeChatMessage.getText());

        sendMessageService.sendMessage(update.getMessage().getChatId(), welcomeChatMessage.getText());
    }

    private boolean hasTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private void handleTextMessage(Update update) {
        String command = extractCommandFromMessage(update.getMessage().getText());
        MessageBotCommand telegramBotCommand = MessageBotCommand.fromString(command);
        Command commandToExecute = commandMap.get(telegramBotCommand);
        if (commandToExecute != null) {
            commandToExecute.execute(update.getMessage());
        }
    }

    private String extractCommandFromMessage(String messageText) {
        return messageText.split(" ")[0];
    }
}
