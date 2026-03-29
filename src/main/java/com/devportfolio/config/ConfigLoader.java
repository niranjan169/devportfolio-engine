package com.devportfolio.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigLoader {

    public static class Config {
        public String theme = "default";
        public String output = "./dist";
    }

    public Config loadConfig(String configPath) {
        Config config = new Config();
        Path path = Path.of(configPath);
        if (!Files.exists(path)) {
            return config;
        }

        try {
            String content = Files.readString(path);
            config.theme = parseValue(content, "theme", config.theme);
            config.output = parseValue(content, "output", config.output);
        } catch (IOException e) {
            // Fallback to default
        }
        return config;
    }

    private String parseValue(String json, String key, String defaultValue) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return defaultValue;
    }
}
