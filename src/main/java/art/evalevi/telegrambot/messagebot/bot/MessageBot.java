package art.evalevi.telegrambot.messagebot.bot;

import art.evalevi.telegrambot.messagebot.command.*;
import art.evalevi.telegrambot.messagebot.dto.ChatDto;
import art.evalevi.telegrambot.messagebot.dto.ChatMessageDto;
import art.evalevi.telegrambot.messagebot.event.SendMessageEvent;
import art.evalevi.telegrambot.messagebot.mapper.ChatMapper;
import art.evalevi.telegrambot.messagebot.mapper.ChatMessageMapper;
import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

/**
 * The class that describes the bot
 */
@Component
public class MessageBot extends TelegramLongPollingBot {

    public static final String DATETIME_FORMAT = "dd-MM-yy HH:mm";
    public static final String CURRENT_TIMEZONE = "Europe/Moscow";

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
     * Method for scheduled message sending
     * Called every 20 seconds
     */
    @Scheduled(cron = "*/20 * * * * *")
    @Async
    public void sendTimedMessages() {

        LocalDateTime currentTime = getCurrentTimeTruncatedToMinutes();

        List<ChatMessage> chatMessages = messageService.findAllByScheduledTime(currentTime);
        chatMessages.forEach(this::processScheduledChatMessage);
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

    private LocalDateTime getCurrentTimeTruncatedToMinutes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(CURRENT_TIMEZONE)).truncatedTo(ChronoUnit.MINUTES);
        zonedDateTime.format(formatter);
        return zonedDateTime.toLocalDateTime();
    }

    private void processScheduledChatMessage(ChatMessage chatMessage) {
        Optional<ChatMessage> chatMessageOptional =
                messageService.findByChatIdAndTypeAndStatus(chatMessage.getChatId(), MessageType.SCHEDULED, MessageStatus.AWAIT);

        if (chatMessageOptional.isPresent()) {
            updateChatMessageStatus(chatMessageOptional.get());
            sendBotMessage(chatMessage.getChatId(), chatMessageOptional.get().getText());
        }
    }

    private void updateChatMessageStatus(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.DONE);
        messageService.saveMessage(chatMessage);
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

//        sendBotMessage(update.getMessage().getChatId(), welcomeChatMessage.getText());

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
