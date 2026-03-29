package com.devportfolio.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MarkdownParser {

    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.*?)\\*\\*");
    private static final Pattern ITALIC_PATTERN = Pattern.compile("\\*(.*?)\\*");
    private static final Pattern LINK_PATTERN = Pattern.compile("\\[(.*?)\\]\\((.*?)\\)");
    private static final Pattern INLINE_CODE_PATTERN = Pattern.compile("`(.*?)`");

    public List<String> parse(List<Token> tokens) {
        List<String> htmlLines = new ArrayList<>();
        boolean inList = false;
        TokenType listType = null;

        for (Token token : tokens) {
            // Close list if current token is not a list item and not a newline
            if (inList && token.type() != listType && token.type() != TokenType.NEWLINE) {
                htmlLines.add(listType == TokenType.LIST_ITEM_UNORDERED ? "</ul>" : "</ol>");
                inList = false;
            }

            switch (token.type()) {
                case HEADER_1 -> htmlLines.add("<h1>" + parseInline(token.value()) + "</h1>");
                case HEADER_2 -> htmlLines.add("<h2>" + parseInline(token.value()) + "</h2>");
                case HEADER_3 -> htmlLines.add("<h3>" + parseInline(token.value()) + "</h3>");
                case LIST_ITEM_UNORDERED -> {
                    if (!inList) {
                        htmlLines.add("<ul>");
                        inList = true;
                        listType = TokenType.LIST_ITEM_UNORDERED;
                    }
                    htmlLines.add("<li>" + parseInline(token.value()) + "</li>");
                }
                case LIST_ITEM_ORDERED -> {
                    if (!inList) {
                        htmlLines.add("<ol>");
                        inList = true;
                        listType = TokenType.LIST_ITEM_ORDERED;
                    }
                    htmlLines.add("<li>" + parseInline(token.value()) + "</li>");
                }
                case CODE_BLOCK -> htmlLines.add("<pre><code>" + token.value() + "</code></pre>");
                case TEXT -> htmlLines.add("<p>" + parseInline(token.value()) + "</p>");
                case NEWLINE -> {
                    // Ignore empty newlines or handle spacing
                }
                default -> {}
            }
        }

        if (inList) {
            htmlLines.add(listType == TokenType.LIST_ITEM_UNORDERED ? "</ul>" : "</ol>");
        }

        return htmlLines;
    }

    private String parseInline(String text) {
        if (text == null) return "";
        String result = text;
        result = BOLD_PATTERN.matcher(result).replaceAll("<strong>$1</strong>");
        result = ITALIC_PATTERN.matcher(result).replaceAll("<em>$1</em>");
        result = LINK_PATTERN.matcher(result).replaceAll("<a href=\"$2\" target=\"_blank\">$1</a>");
        result = INLINE_CODE_PATTERN.matcher(result).replaceAll("<code>$1</code>");
        return result;
    }
}
