package art.evalevi.telegrambot.messagebot.command;

import art.evalevi.telegrambot.messagebot.dto.ChatMessageDto;
import art.evalevi.telegrambot.messagebot.mapper.ChatMessageMapper;
import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import art.evalevi.telegrambot.messagebot.model.MessageType;
import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.MessageService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Class-command for changing timed message
 */
public class ChangeTimedMessageCommand extends AdminCommand {

    public static final String DEFAULT_TIMED_MSG = "Привет!";

    private final MessageService messageService;
    private final SendMessageService sendMessageService;
    private final ChatService chatService;

    public ChangeTimedMessageCommand(MessageService messageService, SendMessageService sendMessageService, ChatService chatService) {
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
        String newTimedMessage = partsMessage[2];

        ChatMessage timedMessage = messageService.findByChatIdAndType(chatId, MessageType.SCHEDULED)
                .orElse(ChatMessageMapper.INSTANCE.messageDtoToMessage(
                        new ChatMessageDto(
                                chatId, MessageType.SCHEDULED, DEFAULT_TIMED_MSG, null, null)));

        timedMessage.setText(newTimedMessage);
        messageService.saveMessage(timedMessage);

        sendMessageService.sendMessage(message.getChatId(), SET_MESSAGE_SUCCESS);
    }
}
