package src.command;

import src.*;

public class ZipContentCommand extends ZipCommand {
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Просмотр содержимого архива.");
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Содержимое архива:");
        for (FileProperties fp : zipFileManager.getFilesList()) {
            ConsoleHelper.writeMessage(fp.toString());
        }
        ConsoleHelper.writeMessage("Содержимое архива прочитано.");
    }
}
