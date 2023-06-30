package art.evalevi.telegrambot.messagebot.command;

import art.evalevi.telegrambot.messagebot.dto.ChatMessageDto;
import art.evalevi.telegrambot.messagebot.mapper.ChatMessageMapper;
import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageStatus;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Class-command for changing scheduled time
 */
public class ChangeScheduledTimeCommand extends AdminCommand {

    public static final String DATETIME_FORMAT = "dd-MM-yy HH:mm";
    public static final String CURRENT_TIMEZONE = "Europe/Moscow";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT).withZone(ZoneId.of(CURRENT_TIMEZONE));

    private final MessageService messageService;
    private final SendMessageService sendMessageService;
    private final ChatService chatService;

    public ChangeScheduledTimeCommand(MessageService messageService, SendMessageService sendMessageService, ChatService chatService) {
        this.messageService = messageService;
        this.sendMessageService = sendMessageService;
        this.chatService = chatService;
    }

    @Override
    public void executeAdmin(Message message) {
        String[] partsMessage = parseMessage(message);

        if (partsMessage.length < PARTS_MESSAGE_EXPECTED_SIZE) {
            // TODO handle wrong message format
            return;
        }

        Long chatId = Long.parseLong(partsMessage[1]);

        if (chatService.findByChatId(chatId).isEmpty()) {
            sendMessageService.sendMessage(message.getChatId(), SET_MESSAGE_FAIL);
            return;
        }
        String newScheduledTimeStr = partsMessage[2];

        ChatMessage timedMessage = messageService.findByChatIdAndType(chatId, MessageType.SCHEDULED)
                .orElse(ChatMessageMapper.INSTANCE.messageDtoToMessage(
                        new ChatMessageDto(
                                chatId, MessageType.SCHEDULED, "", null, null)));

        try {
            LocalDateTime newScheduledTime = parseScheduledTime(newScheduledTimeStr);
            timedMessage.setScheduledTime(newScheduledTime);
            timedMessage.setStatus(MessageStatus.AWAIT);

            messageService.saveMessage(timedMessage);
            sendMessageService.sendMessage(message.getChatId(), SET_MESSAGE_SUCCESS);

        } catch (DateTimeParseException e) {
            // TODO handle wrong date/time format
            sendMessageService.sendMessage(message.getChatId(), SET_MESSAGE_FAIL);
        }
    }

    private LocalDateTime parseScheduledTime(String scheduledTimeStr) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(scheduledTimeStr, FORMATTER).truncatedTo(ChronoUnit.MINUTES);
        return zonedDateTime.toLocalDateTime();
    }
}
