package src;

import src.exception.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class ZipFileManager {
    private final Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        try (InputStream inputStream = Files.newInputStream(filePath.resolve(fileName))) {
            ZipEntry zipEntry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(zipEntry);
            copyData(inputStream, zipOutputStream);
            zipOutputStream.closeEntry();
        }
    }

    public void createZip(Path source) throws Exception {
        if (!Files.exists(zipFile.getParent()))
            Files.createDirectory(zipFile.getParent());
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(
                Files.newOutputStream(zipFile))) {
            if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();
                for (Path fileName : fileNames) {
                    addNewZipEntry(zipOutputStream, source, fileName);
                }
            } else throw new PathIsNotFoundException();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int balance;
        while ((balance = in.read(buffer)) > 0) {
            out.write(buffer, 0, balance);
        }
    }

    public List<FileProperties> getFilesList() throws Exception {
        if (!Files.isRegularFile(zipFile))
            throw new WrongZipFileException();
        else {
            List<FileProperties> propertiesList = new ArrayList<>();
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
                ZipEntry tempEntry;
                while ((tempEntry = zipInputStream.getNextEntry()) != null) {
                    ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
                    copyData(zipInputStream, tempBuffer);
                    String name = tempEntry.getName();
                    long size = tempEntry.getSize();
                    long compressedSize = tempEntry.getCompressedSize();
                    int compressionMethod = tempEntry.getMethod();
                    FileProperties fileProperties = new FileProperties(name, size, compressedSize, compressionMethod);
                    propertiesList.add(fileProperties);
                }
            }
            return propertiesList;
        }
    }

    public void extractAll(Path outputFolder) throws Exception {
        if (!Files.isRegularFile(zipFile))
            throw new WrongZipFileException();

        try (ZipInputStream zipInputStream = new ZipInputStream(
                Files.newInputStream(zipFile))) {

            if (Files.notExists(outputFolder))
                Files.createDirectories(outputFolder);

            ZipEntry tempEntry;
            while ((tempEntry = zipInputStream.getNextEntry()) != null) {
                Path filePath = outputFolder.resolve(tempEntry.getName());
                Path parent = filePath.getParent();
                if (Files.notExists(parent))
                    Files.createDirectories(parent);
                try (OutputStream out = Files.newOutputStream(filePath)) {
                    copyData(zipInputStream, out);
                }
            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) throw new WrongZipFileException();
        Path tempZip = Files.createTempFile("temp", ".tmp");
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZip))) {

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path path = Paths.get(entry.getName());
                if (pathList.contains(path)) {
                    ConsoleHelper.writeMessage("Файл удален: " + path);
                } else {
                    zipOutputStream.putNextEntry(entry);
                    copyData(zipInputStream, zipOutputStream);
                    zipInputStream.closeEntry();
                    zipOutputStream.closeEntry();
                }
            }
            Files.move(tempZip, zipFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        Path temp = Files.createTempFile(null, null);
        List<Path> tempPathList = new ArrayList<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile));
             ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(temp))) {

            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path archivedFile = Paths.get(zipEntry.getName());
                zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));
                copyData(zipInputStream, zipOutputStream);
                tempPathList.add(archivedFile);
            }

            for (Path file : absolutePathList) {
                if (Files.notExists(file)) {
                    throw new PathIsNotFoundException();
                }

                if (!tempPathList.contains(file.getFileName())) {
                    addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());
                    ConsoleHelper.writeMessage(file.getFileName().toString() + " добавлен в архив");
                } else {
                    ConsoleHelper.writeMessage(file.getFileName().toString() + " уже есть в архиве");
                }
            }
            zipInputStream.closeEntry();
            zipOutputStream.closeEntry();
        }
        Files.move(temp, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }
}
