package com.devportfolio.renderer;

import java.util.List;

public class HtmlRenderer {

    public String render(List<String> bodyContent, String cssContent, String title) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>").append(title).append("</title>\n")
            .append("    <style>\n").append(cssContent).append("\n    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <div class=\"container\">\n");

        boolean inHeader = false;
        boolean inSection = false;
        boolean inGrid = false;
        boolean inTimeline = false;
        String currentSectionTitle = "";

        for (String line : bodyContent) {
            String trimmed = line.trim();

            // Hero Section Handling
            if (trimmed.startsWith("<h1>")) {
                if (inSection) {
                    if (inGrid || inTimeline) html.append("            </div>\n");
                    html.append("        </section>\n");
                    inGrid = inTimeline = false;
                }
                html.append("        <header class=\"hero\">\n");
                html.append("            <h1 class=\"name\">").append(stripTags(line)).append("</h1>\n");
                inHeader = true;
                inSection = false;
                continue;
            }

            if (trimmed.startsWith("<h2>")) {
                if (inHeader) {
                    html.append("            <p class=\"role\">").append(stripTags(line)).append("</p>\n");
                    html.append("            <div class=\"contact-links\">\n");
                    continue;
                }
                if (inSection) {
                    if (inGrid || inTimeline) html.append("            </div>\n");
                    html.append("        </section>\n");
                    inGrid = inTimeline = false;
                }
                html.append("        <section class=\"section\">\n");
                html.append("            <h2>").append(stripTags(line)).append("</h2>\n");
                inSection = true;
                continue;
            }

            // Close Header if a new section (h3) starts
            if (trimmed.startsWith("<h3>")) {
                if (inHeader) {
                    html.append("            </div>\n");
                    html.append("        </header>\n");
                    inHeader = false;
                }
                if (inSection) {
                    if (inGrid || inTimeline) html.append("            </div>\n");
                    html.append("        </section>\n");
                    inGrid = inTimeline = false;
                }
                
                currentSectionTitle = stripTags(line).toLowerCase();
                String sectionClass = currentSectionTitle.replace(" ", "-") + "-section";
                html.append("        <section class=\"section ").append(sectionClass).append("\">\n");
                html.append("            <h2>").append(stripTags(line)).append("</h2>\n");
                inSection = true;

                if (currentSectionTitle.contains("skills")) {
                    html.append("            <div class=\"skills-grid\">\n");
                    inGrid = true;
                } else if (currentSectionTitle.contains("projects")) {
                    html.append("            <div class=\"projects-grid\">\n");
                    inGrid = true;
                } else if (currentSectionTitle.contains("experience") || currentSectionTitle.contains("education")) {
                    html.append("            <div class=\"timeline\">\n");
                    inTimeline = true;
                }
                continue;
            }

            // Contact Links in Header
            if (inHeader && (trimmed.contains("|") || trimmed.contains("<a "))) {
                html.append("                ").append(line.replace("<a ", "<a class=\"btn\" ")).append("\n");
                continue;
            }

            // List Item Handling
            if (trimmed.startsWith("<li>")) {
                String content = line.substring(line.indexOf("<li>") + 4, line.lastIndexOf("</li>"));
                if (inGrid) {
                    String cardClass = currentSectionTitle.contains("projects") ? "project-card" : "card";
                    html.append("                <div class=\"").append(cardClass).append("\">\n")
                        .append("                    ").append(content).append("\n")
                        .append("                </div>\n");
                    continue;
                } else if (inTimeline) {
                    html.append("                <div class=\"timeline-item\">\n")
                        .append("                    ").append(content).append("\n")
                        .append("                </div>\n");
                    continue;
                }
            }

            // Clean up list tags in specialized sections
            if ((inGrid || inTimeline) && (trimmed.equals("<ul>") || trimmed.equals("<ol>") || trimmed.equals("</ul>") || trimmed.equals("</ol>"))) {
                continue;
            }

            // Button Group for Links Section
            if (currentSectionTitle.contains("links") || currentSectionTitle.contains("contact")) {
                if (trimmed.equals("<ul>") || trimmed.equals("<ol>")) {
                    html.append("            <div class=\"button-group\">\n");
                    continue;
                }
                if (trimmed.equals("</ul>") || trimmed.equals("</ol>")) {
                    html.append("            </div>\n");
                    continue;
                }
                if (trimmed.contains("<a ")) {
                    html.append("                ").append(line.replace("<a ", "<a class=\"btn\" ")).append("\n");
                    continue;
                }
            }

            html.append("            ").append(line).append("\n");
        }

        if (inHeader) {
            html.append("            </div>\n");
            html.append("        </header>\n");
        }
        if (inSection) {
            if (inGrid || inTimeline) html.append("            </div>\n");
            html.append("        </section>\n");
        }

        html.append("    </div>\n")
            .append("</body>\n")
            .append("</html>");

        return html.toString();
    }

    private String stripTags(String html) {
        return html.replaceAll("<[^>]*>", "").trim();
    }
}
