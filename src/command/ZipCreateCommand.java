package src.command;

import src.ZipFileManager;
import src.ConsoleHelper;
import src.exception.PathIsNotFoundException;

import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand {
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Создание архива.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите полное имя файла или директории для архивации");
            String path = ConsoleHelper.readString();
            zipFileManager.createZip(Paths.get(path));
            ConsoleHelper.writeMessage("Архив создан.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
