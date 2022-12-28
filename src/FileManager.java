package src;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.io.IOException;

public class FileManager {
    private final Path rootPath;
    private final List<Path> fileList = new ArrayList<>();

    public FileManager(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        collectFileList(rootPath);
    }

    public List<Path> getFileList() {
        return this.fileList;
    }

    private void collectFileList(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            fileList.add(this.rootPath.relativize(path));
        }
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path subPath : stream) {
                    collectFileList(subPath);
                }
            }
        }
    }
}
