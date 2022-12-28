package src.command;

import src.*;
import src.exception.WrongZipFileException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Распаковка архива.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Введите полный путь до дирректории для распаковки:");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(path);
            ConsoleHelper.writeMessage("Распаковка завершена.");
        } catch (WrongZipFileException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
