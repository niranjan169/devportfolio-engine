package com.devportfolio.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileUtils {

    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath));
    }

    public static void writeString(String filePath, String content) throws IOException {
        Path path = Path.of(filePath);
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
