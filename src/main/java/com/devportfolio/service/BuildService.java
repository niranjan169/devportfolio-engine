package com.devportfolio.service;

import com.devportfolio.parser.MarkdownParser;
import com.devportfolio.parser.Tokenizer;
import com.devportfolio.renderer.HtmlRenderer;
import com.devportfolio.theme.ThemeLoader;
import com.devportfolio.util.FileUtils;

import java.io.IOException;
import java.util.List;

public class BuildService {

    private final Tokenizer tokenizer = new Tokenizer();
    private final MarkdownParser parser = new MarkdownParser();
    private final HtmlRenderer renderer = new HtmlRenderer();
    private final ThemeLoader themeLoader = new ThemeLoader();
    private final PdfService pdfService = new PdfService();

    public void build(String inputFile, String outputDir, String themeName) {
        try {
            List<String> lines;
            if (inputFile.toLowerCase().endsWith(".pdf")) {
                lines = pdfService.processPdf(inputFile);
                System.out.println("Generated Markdown:");
                lines.forEach(System.out::println);
                System.out.println("-------------------");
            } else {
                lines = FileUtils.readLines(inputFile);
            }
            var tokens = tokenizer.tokenize(lines);
            var htmlContent = parser.parse(tokens);
            String css = themeLoader.loadTheme(themeName);
            
            String title = "DevPortfolio - " + inputFile;
            String finalHtml = renderer.render(htmlContent, css, title);
            
            String outputPath = outputDir + (outputDir.endsWith("/") ? "" : "/") + "index.html";
            FileUtils.writeString(outputPath, finalHtml);
            System.out.println("\u001B[32mBuild successful:\u001B[0m " + outputPath);
        } catch (IOException e) {
            System.err.println("\u001B[31mBuild failed:\u001B[0m " + e.getMessage());
        }
    }
}
