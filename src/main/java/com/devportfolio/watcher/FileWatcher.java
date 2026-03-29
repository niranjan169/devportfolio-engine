package com.devportfolio.watcher;

import com.devportfolio.service.BuildService;
import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    private final BuildService buildService;

    public FileWatcher(BuildService buildService) {
        this.buildService = buildService;
    }

    public void watch(String inputFile, String outputDir, String themeName) {
        Path path = Path.of(inputFile).toAbsolutePath();
        Path parent = path.getParent();

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            parent.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            System.out.println("\u001B[34mWatching for changes in:\u001B[0m " + inputFile);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = (Path) event.context();
                    if (changed.equals(path.getFileName())) {
                        System.out.println("Change detected, rebuilding...");
                        buildService.build(inputFile, outputDir, themeName);
                    }
                }
                if (!key.reset()) break;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Watcher error: " + e.getMessage());
        }
    }
}
