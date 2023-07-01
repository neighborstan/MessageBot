package art.evalevi.telegrambot.messagebot.command;

import art.evalevi.telegrambot.messagebot.service.ChatService;
import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Class-command for displaying all groups in which the bot is a member
 */
public class ShowAllGroupsCommand extends AdminCommand {

    public static final String ACTUAL_GROUPS_TITLES = "Актуальные группы:\n";

    private final ChatService chatService;
    private final SendMessageService sendMessageService;

    public ShowAllGroupsCommand(ChatService chatService, SendMessageService sendMessageService) {
        this.chatService = chatService;
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void executeAdmin(Message message) {

        StringBuilder actualGroups = new StringBuilder();
        chatService.findAll().forEach(chat -> actualGroups.append("<code>%s, ID >>>  %d</code>\n".formatted(chat.getName(), chat.getChatId())));

        sendMessageService.sendMessage(message.getChatId(), ACTUAL_GROUPS_TITLES + actualGroups);

    }
}
