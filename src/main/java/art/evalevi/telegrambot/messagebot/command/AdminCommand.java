package art.evalevi.telegrambot.messagebot.command;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/**
 * Wrapper class for checking admin rights for executing commands
 */
public abstract class AdminCommand implements Command {

    public static final int PARTS_MESSAGE_EXPECTED_SIZE = 3;
    public static final String SET_MESSAGE_SUCCESS = "It's set!";
    public static final String SET_MESSAGE_FAIL = "Fail. Check ID or data format.";

    private final List<String> admins = List.of("neighbor", "azno777"); // TODO move to properties

    protected abstract void executeAdmin(Message message);

    /**
     * Checks if user is admin and executes command
     * @param message message from user
     */
    public void execute(Message message) {
        String username = message.getFrom().getUserName();

        if (isAdmin(username)) {
            executeAdmin(message);
        }
    }

    protected String[] parseMessage(Message message) {
        return message.getText().split(" ", PARTS_MESSAGE_EXPECTED_SIZE);
    }

    protected boolean isAdmin(String username) {
        return admins.contains(username);
    }
}
