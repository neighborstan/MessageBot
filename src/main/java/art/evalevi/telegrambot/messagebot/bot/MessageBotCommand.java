package art.evalevi.telegrambot.messagebot.bot;

/**
 * Enum for bot commands
 */
public enum MessageBotCommand {
    CHANGE_WELCOME("/welcomemsg"),
    CHANGE_TIMED_MESSAGE("/plannedmsg"),
    CHANGE_SCHEDULED_TIME("/scheduledtime"),
    SHOW_ALL_GROUPS("/groups"),
    SHOW_INFO("/help");

    private final String command;

    MessageBotCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static MessageBotCommand fromString(String commandString) {
        for (MessageBotCommand messageBotCommand : MessageBotCommand.values()) {
            if (messageBotCommand.getCommand().equalsIgnoreCase(commandString)) {
                return messageBotCommand;
            }
        }
        return null;
    }
}

