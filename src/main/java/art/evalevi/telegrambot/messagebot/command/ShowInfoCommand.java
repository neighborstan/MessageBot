package art.evalevi.telegrambot.messagebot.command;

import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Class-command for displaying information on working with the bot
 */
public class ShowInfoCommand extends AdminCommand {

    public static final String HELP_INFO_MSG = """
            Command templates for editing:
            - welcome message
            <code>/welcomemsg [group ID] [New message]</code>
                        
            - scheduled message
            <code>/plannedmsg [group ID] [New message]</code>
                        
            - scheduled time
            <code>/scheduledtime [group ID] [DD-MM-YY hh:mm]</code>
                        
            ===== ===== =====
                        
            List bot groups:
            <code>/groups</code>
            """;

    private final SendMessageService sendMessageService;

    public ShowInfoCommand(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void executeAdmin(Message message) {
        sendMessageService.sendMessage(message.getChatId(), HELP_INFO_MSG);
    }
}
