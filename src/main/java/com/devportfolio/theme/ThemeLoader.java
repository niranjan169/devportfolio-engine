package com.devportfolio.theme;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ThemeLoader {

    public String loadTheme(String themeName) {
        String resourcePath = "/themes/" + themeName + ".css";
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                // Fallback to default
                if (!themeName.equals("default")) {
                    return loadTheme("default");
                }
                return getDefaultStyles();
            }
            return new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return getDefaultStyles();
        }
    }

    private String getDefaultStyles() {
        return "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; line-height: 1.6; color: #333; max-width: 800px; margin: 40px auto; padding: 0 20px; }";
    }
}
