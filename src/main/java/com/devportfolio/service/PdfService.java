package com.devportfolio.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfService {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "((https?|ftp|file)://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    private static final List<String> SECTION_KEYWORDS = List.of(
            "SUMMARY", "EDUCATION", "SKILLS", "PROJECTS", "EXPERIENCE", "LINKS", "CONTACT", "ACHIEVEMENTS", "LANGUAGES",
            "PROFESSIONAL SUMMARY", "INTERNSHIP EXPERIENCE", "TECHNICAL SKILLS", "AWARDS", "CERTIFICATIONS"
    );

    public List<String> processPdf(String filePath) throws IOException {
        String rawText = extractText(filePath);
        return convertToMarkdown(rawText);
    }

    private String extractText(String filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            if (document.isEncrypted()) {
                throw new IOException("Cannot process encrypted PDF");
            }
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private List<String> convertToMarkdown(String rawText) {
        String[] lines = rawText.split("\\r?\\n");
        List<String> markdownLines = new ArrayList<>();
        boolean nameFound = false;
        String lastSectionTitle = "";

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                if (!markdownLines.isEmpty() && !markdownLines.get(markdownLines.size() - 1).isEmpty()) {
                    markdownLines.add("");
                }
                continue;
            }

            // Rule 1: First non-empty line as Name
            if (!nameFound) {
                markdownLines.add("# " + trimmedLine);
                markdownLines.add("");
                nameFound = true;
                continue;
            }

            // Rule 5: URL Detection (Applied before other rules to preserve links in sections/bullets)
            trimmedLine = convertUrlsToLinks(trimmedLine);

            // Rule 2: Section Detection
            String upperLine = trimmedLine.toUpperCase();
            boolean isSection = false;
            for (String keyword : SECTION_KEYWORDS) {
                if (upperLine.contains(keyword) && trimmedLine.length() < 40) {
                    String capitalized = capitalize(trimmedLine);
                    if (!capitalized.equals(lastSectionTitle)) {
                        markdownLines.add("## " + capitalized);
                        lastSectionTitle = capitalized;
                    }
                    isSection = true;
                    break;
                }
            }
            if (isSection) continue;

            // Rule 3: Mostly Uppercase -> Heading
            if (isMostlyUppercase(trimmedLine)) {
                markdownLines.add("## " + trimmedLine);
                continue;
            }

            // Rule 4: Bullet Point Detection
            if (trimmedLine.startsWith("•") || trimmedLine.startsWith("-") || trimmedLine.startsWith("*")) {
                String content = trimmedLine.substring(1).trim();
                markdownLines.add("* " + content);
                continue;
            }

            // Rule 6: Normal Text
            markdownLines.add(trimmedLine);
        }

        return markdownLines;
    }

    private boolean isMostlyUppercase(String line) {
        if (line.length() < 3 || line.length() > 60) return false;
        int upperCount = 0;
        int letterCount = 0;
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCount++;
                if (Character.isUpperCase(c)) upperCount++;
            }
        }
        return letterCount > 0 && (double) upperCount / letterCount > 0.8;
    }

    private String convertUrlsToLinks(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            sb.append(text, lastEnd, matcher.start());
            String url = matcher.group();
            String displayUrl = url;
            if (url.contains("linkedin.com")) displayUrl = "LinkedIn";
            else if (url.contains("github.com")) displayUrl = "GitHub";
            
            String targetUrl = url.startsWith("www.") ? "http://" + url : url;
            sb.append("[").append(displayUrl).append("](").append(targetUrl).append(")");
            lastEnd = matcher.end();
        }
        sb.append(text.substring(lastEnd));
        return sb.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            result.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
        }
        return result.toString().trim();
    }
}
