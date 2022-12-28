package src;

import src.exception.*;

import java.io.IOException;



public class Archiver {
    public static void main(String[] args) {
        Operation operation = null;
        while (operation != Operation.EXIT) {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception j) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }
        }
    }

    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Выберите операцию:");
        ConsoleHelper.writeMessage(Operation.CREATE.ordinal() + " - упаковать файлы в архив");
        ConsoleHelper.writeMessage(Operation.ADD.ordinal() + " - добавить файл в архив");
        ConsoleHelper.writeMessage(Operation.REMOVE.ordinal() + " - удалить файл из архива");
        ConsoleHelper.writeMessage(Operation.EXTRACT.ordinal() + " - распаковать архив");
        ConsoleHelper.writeMessage(Operation.CONTENT.ordinal() + " - просмотреть содержимое архива");
        ConsoleHelper.writeMessage(Operation.EXIT.ordinal() + " - выход");
        return Operation.values()[ConsoleHelper.readInt()];
    }
}
