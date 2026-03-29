package com.devportfolio.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final Pattern H1_PATTERN = Pattern.compile("^#\\s+(.*)");
    private static final Pattern H2_PATTERN = Pattern.compile("^##\\s+(.*)");
    private static final Pattern H3_PATTERN = Pattern.compile("^###\\s+(.*)");
    private static final Pattern UL_PATTERN = Pattern.compile("^[*-]\\s+(.*)");
    private static final Pattern OL_PATTERN = Pattern.compile("^\\d+\\.\\s+(.*)");
    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("^```(.*)");

    public List<Token> tokenize(List<String> lines) {
        List<Token> tokens = new ArrayList<>();
        boolean inCodeBlock = false;
        StringBuilder codeContent = new StringBuilder();

        for (String line : lines) {
            if (inCodeBlock) {
                Matcher codeMatcher = CODE_BLOCK_PATTERN.matcher(line);
                if (codeMatcher.matches()) {
                    tokens.add(new Token(TokenType.CODE_BLOCK, codeContent.toString().trim()));
                    codeContent.setLength(0);
                    inCodeBlock = false;
                } else {
                    codeContent.append(line).append("\n");
                }
                continue;
            }

            Matcher m1 = H1_PATTERN.matcher(line);
            if (m1.matches()) {
                tokens.add(new Token(TokenType.HEADER_1, m1.group(1)));
                continue;
            }

            Matcher m2 = H2_PATTERN.matcher(line);
            if (m2.matches()) {
                tokens.add(new Token(TokenType.HEADER_2, m2.group(1)));
                continue;
            }

            Matcher m3 = H3_PATTERN.matcher(line);
            if (m3.matches()) {
                tokens.add(new Token(TokenType.HEADER_3, m3.group(1)));
                continue;
            }

            Matcher ul = UL_PATTERN.matcher(line);
            if (ul.matches()) {
                tokens.add(new Token(TokenType.LIST_ITEM_UNORDERED, ul.group(1)));
                continue;
            }

            Matcher ol = OL_PATTERN.matcher(line);
            if (ol.matches()) {
                tokens.add(new Token(TokenType.LIST_ITEM_ORDERED, ol.group(1)));
                continue;
            }

            Matcher codeBlock = CODE_BLOCK_PATTERN.matcher(line);
            if (codeBlock.matches()) {
                inCodeBlock = true;
                continue;
            }

            if (line.trim().isEmpty()) {
                tokens.add(new Token(TokenType.NEWLINE, ""));
            } else {
                tokens.add(new Token(TokenType.TEXT, line));
            }
        }

        return tokens;
    }
}
