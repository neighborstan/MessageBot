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
 * Class-command for changing welcome message
 */
public class ChangeWelcomeCommand extends AdminCommand {

    public static final String DEFAULT_WELCOME_MSG = "Привет!";

    private final MessageService messageService;
    private final SendMessageService sendMessageService;
    private final ChatService chatService;

    public ChangeWelcomeCommand(MessageService messageService, SendMessageService sendMessageService, ChatService chatService) {
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
        String newWelcomeMessage = partsMessage[2];

        ChatMessage welcomeMessage = messageService.findByChatIdAndType(chatId, MessageType.WELCOME)
                .orElse(ChatMessageMapper.INSTANCE.messageDtoToMessage(
                        new ChatMessageDto(
                                chatId, MessageType.WELCOME, DEFAULT_WELCOME_MSG, null, null)));

        welcomeMessage.setText(newWelcomeMessage);
        messageService.saveMessage(welcomeMessage);

        sendMessageService.sendMessage(message.getChatId(), SET_MESSAGE_SUCCESS);
    }
}

