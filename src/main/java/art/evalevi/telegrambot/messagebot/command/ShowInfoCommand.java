package art.evalevi.telegrambot.messagebot.command;

import art.evalevi.telegrambot.messagebot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Class-command for displaying information on working with the bot
 */
public class ShowInfoCommand extends AdminCommand {

    public static final String HELP_INFO_MSG = """
            Шаблоны команд для редактирования:
            - приветственного сообщения
            <code>/welcomemsg [ID группы] [Новое сообщение]</code>
                        
            - запланированного сообщения
            <code>/plannedmsg [ID группы] [Новое сообщение]</code>
                        
            - запланированного времени
            <code>/scheduledtime [ID группы] [ДД-ММ-ГГ чч:мм]</code>
                        
            ===== ===== =====
                        
            Вывести список групп бота:
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
