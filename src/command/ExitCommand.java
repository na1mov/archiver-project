package src.command;

import src.ConsoleHelper;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        ConsoleHelper.writeMessage("До встречи!");
    }
}
