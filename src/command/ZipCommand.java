package src.command;

import src.ZipFileManager;
import src.ConsoleHelper;

import java.nio.file.Paths;

public abstract class ZipCommand implements Command {
    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Введите полный путь файла архива:");
        String path = ConsoleHelper.readString();
        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(path));
        return zipFileManager;
    }
}
