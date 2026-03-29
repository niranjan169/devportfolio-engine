package com.devportfolio;

import com.devportfolio.config.ConfigLoader;
import com.devportfolio.service.BuildService;
import com.devportfolio.watcher.FileWatcher;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("\u001B[1m\u001B[35mDevPortfolio Engine v1.0\u001B[0m");

        if (args.length == 0) {
            printUsage();
            return;
        }

        String inputFile = args[0];
        Map<String, String> flags = parseFlags(args);

        ConfigLoader configLoader = new ConfigLoader();
        ConfigLoader.Config config = configLoader.loadConfig("config.json");

        String theme = flags.getOrDefault("theme", config.theme);
        String output = flags.getOrDefault("output", config.output);
        boolean watch = flags.containsKey("watch");

        BuildService buildService = new BuildService();
        buildService.build(inputFile, output, theme);

        if (watch) {
            FileWatcher watcher = new FileWatcher(buildService);
            watcher.watch(inputFile, output, theme);
        }
    }

    private static Map<String, String> parseFlags(String[] args) {
        Map<String, String> flags = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String flag = args[i].substring(2);
                if (flag.contains("=")) {
                    String[] parts = flag.split("=", 2);
                    flags.put(parts[0], parts[1]);
                } else {
                    flags.put(flag, "true");
                }
            }
        }
        return flags;
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar devportfolio.jar <input.md|input.pdf> [flags]");
        System.out.println("\nOptions:");
        System.out.println("  --theme=<name>   Specify theme (default, dark, minimal)");
        System.out.println("  --output=<dir>    Specify output directory");
        System.out.println("  --watch           Enable live preview mode");
        System.out.println("\nExample:");
        System.out.println("  java -jar devportfolio.jar resume.md --theme=dark --watch");
        System.out.println("  java -jar devportfolio.jar resume.pdf --theme=minimal");
    }
}
