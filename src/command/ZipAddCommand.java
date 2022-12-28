package src.command;

import src.*;
import src.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Добавление файла в архив.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите полный путь файла для добавления");
            Path sourcePath = Paths.get(ConsoleHelper.readString());
            zipFileManager.addFile(sourcePath);
            ConsoleHelper.writeMessage("Добавление в архив завершено.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Что-то пошло не такю");
        }
    }
}
